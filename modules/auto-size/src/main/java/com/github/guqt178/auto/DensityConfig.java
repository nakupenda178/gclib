package com.github.guqt178.auto;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.github.guqt178.auto.external.ExternalAdapterManager;
import com.github.guqt178.auto.utils.Preconditions;
import com.github.guqt178.auto.utils.ScreenUtils;

/**
 *  DensityConfig.getInstance()
 * //                .setCustomFragment(true)
 * //                .setUseDeviceSize(true)
 * //                .setBaseOnWidth(false)
 * //                .setAutoAdaptStrategy(new AutoAdaptStrategy())
 */
public final class DensityConfig {
    private static volatile DensityConfig sInstance;
    private static final String KEY_DESIGN_WIDTH_IN_DP = "design_width_in_dp";
    private static final String KEY_DESIGN_HEIGHT_IN_DP = "design_height_in_dp";
    private Application mApplication;
    /**
     * 用来管理外部三方库 {@link Activity} 的适配
     */
    private ExternalAdapterManager mExternalAdaptManager = new ExternalAdapterManager();
    /**
     * 最初的 {@link DisplayMetrics#density}
     */
    private float mInitDensity = -1;
    /**
     * 最初的 {@link DisplayMetrics#densityDpi}
     */
    private int mInitDensityDpi;
    /**
     * 最初的 {@link DisplayMetrics#scaledDensity}
     */
    private float mInitScaledDensity;
    /**
     * 设计图上的总宽度, 单位 dp
     */
    private int mDesignWidthInDp;
    /**
     * 设计图上的总高度, 单位 dp
     */
    private int mDesignHeightInDp;
    /**
     * 设备的屏幕总宽度, 单位 px
     */
    private int mScreenWidth;
    /**
     * 设备的屏幕总高度, 单位 px, 如果 {@link #isUseDeviceSize} 为 {@code false}, 屏幕总高度会减去状态栏的高度
     * 如果有导航栏也会减去导航栏的高度
     */
    private int mScreenHeight;
    /**
     * 为了保证在不同高宽比的屏幕上显示效果也能完全一致, 所以本方案适配时是以设计图宽度与设备实际宽度的比例或设计图高度与设备实际高度的比例应用到
     * 每个 View 上 (只能在宽度和高度之中选一个作为基准), 从而使每个 View 的高和宽用同样的比例缩放, 避免在与设计图高宽比不一致的设备上出现适配的 View 高或宽变形的问题
     * {@link #isBaseOnWidth} 为 {@code true} 时代表以宽度等比例缩放, {@code false} 代表以高度等比例缩放
     * {@link #isBaseOnWidth} 为全局配置, 默认为 {@code true}, 每个 {@link Activity} 也可以单独选择使用高或者宽做等比例缩放
     */
    private boolean isBaseOnWidth = true;
    /**
     * 此字段表示是否使用设备的实际尺寸做适配
     * {@link #isUseDeviceSize} 为 {@code true} 表示屏幕高度 {@link #mScreenHeight} 包含状态栏的高度, 如果有导航栏也会包含导航栏的高度
     * {@link #isUseDeviceSize} 为 {@code false} 表示 {@link #mScreenHeight} 会减去状态栏的高度, 如果有导航栏也会减去导航栏的高度, 默认为 {@code true}
     */
    private boolean isUseDeviceSize = true;
    /**
     * {@link #mActivityLifecycleCallbacks} 可用来代替在 BaseActivity 中加入适配代码的传统方式
     * {@link #mActivityLifecycleCallbacks} 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 {@link Activity}
     */
    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacks;
    /**
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     *
     * @see #stop(Activity)
     * @see #restart()
     */
    private boolean isStop;
    /**
     * 是否让框架支持自定义 {@link Fragment} 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
     */
    private boolean isCustomFragment;

    public static DensityConfig getInstance() {
        if (sInstance == null) {
            synchronized (DensityConfig.class) {
                if (sInstance == null) {
                    sInstance = new DensityConfig();
                }
            }
        }
        return sInstance;
    }

    private DensityConfig() {
    }

    public Application getApplication() {
        Preconditions.checkNotNull(mApplication, "Please call the AutoSizeConfig#init() first");
        return mApplication;
    }

    /**
     * v0.6.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     * 此方法默认使用以宽度进行等比例适配, 如想使用以高度进行等比例适配, 请调用 {@link #init(Application, boolean)}
     *
     * @param application {@link Application}
     */
    public DensityConfig init(Application application) {
        return init(application, true, null);
    }

    /**
     * v0.6.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     * 此方法使用默认的 {@link AutoAdaptStrategy} 策略, 如想使用自定义的 {@link AutoAdaptStrategy} 策略
     * 请调用 {@link #init(Application, boolean, AutoAdaptStrategy)}
     *
     * @param application   {@link Application}
     * @param isBaseOnWidth 详情请查看 {@link #isBaseOnWidth} 的注释
     */
    public DensityConfig init(Application application, boolean isBaseOnWidth) {
        return init(application, isBaseOnWidth, null);
    }

    /**
     * v0.6.0 以后, 框架会在 APP 启动时自动调用此方法进行初始化, 使用者无需手动初始化, 初始化方法只能调用一次, 否则报错
     *
     * @param application   {@link Application}
     * @param isBaseOnWidth 详情请查看 {@link #isBaseOnWidth} 的注释
     * @param strategy      {@link AutoAdaptStrategy}, 传 {@code null} 则使用 {@link DefaultAutoAdaptStrategy}
     */
    public DensityConfig init(final Application application, boolean isBaseOnWidth, AutoAdaptStrategy strategy) {
        Preconditions.checkArgument(mInitDensity == -1, "AutoSizeConfig#init() can only be called once");
        Preconditions.checkNotNull(application, "application == null");
        this.mApplication = application;
        this.isBaseOnWidth = isBaseOnWidth;
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        getMetaData(application);
        int[] screenSize = ScreenUtils.getScreenSize(application);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];

        mInitDensity = displayMetrics.density;
        mInitDensityDpi = displayMetrics.densityDpi;
        mInitScaledDensity = displayMetrics.scaledDensity;
        application.registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                if (newConfig != null) {
                    if (newConfig.fontScale > 0) {
                        mInitScaledDensity =
                                Resources.getSystem().getDisplayMetrics().scaledDensity;
                    }
                    int[] screenSize = ScreenUtils.getScreenSize(application);
                    mScreenWidth = screenSize[0];
                    mScreenHeight = screenSize[1];
                }
            }

            @Override
            public void onLowMemory() {

            }
        });
        mActivityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl(strategy == null ? new DefaultAutoAdaptStrategy() : strategy);
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        return this;
    }

    /**
     * 重新开始框架的运行
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     */
    public void restart() {
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        synchronized (DensityConfig.class) {
            if (isStop) {
                mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                isStop = false;
            }
        }
    }

    /**
     * 停止框架的运行
     * 框架具有 热插拔 特性, 支持在项目运行中动态停止和重新启动适配功能
     */
    public void stop(Activity activity) {
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        synchronized (DensityConfig.class) {
            if (!isStop) {
                mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                AutoSize.cancelAdapt(activity);
                isStop = true;
            }
        }
    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy {@link AutoAdaptStrategy}
     */
    public DensityConfig setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        Preconditions.checkNotNull(autoAdaptStrategy, "autoAdaptStrategy == null");
        Preconditions.checkNotNull(mActivityLifecycleCallbacks, "Please call the AutoSizeConfig#init() first");
        mActivityLifecycleCallbacks.setAutoAdaptStrategy(autoAdaptStrategy);
        return this;
    }

    /**
     * 是否全局按照宽度进行等比例适配
     *
     * @param baseOnWidth {@code true} 为按照宽度, {@code false} 为按照高度
     * @see #isBaseOnWidth 详情请查看这个字段的注释
     */
    public DensityConfig setBaseOnWidth(boolean baseOnWidth) {
        isBaseOnWidth = baseOnWidth;
        return this;
    }

    /**
     * 是否使用设备的实际尺寸做适配
     *
     * @param useDeviceSize {@code true} 为使用设备的实际尺寸 (包含状态栏, 导航栏), {@code false} 为不使用 (不包含状态栏, 导航栏)
     * @see #isUseDeviceSize 详情请查看这个字段的注释
     */
    public DensityConfig setUseDeviceSize(boolean useDeviceSize) {
        isUseDeviceSize = useDeviceSize;
        return this;
    }

    /**
     * 是否打印 Log
     *
     * @param log {@code true} 为打印
     */
    public DensityConfig setLog(boolean log) {
        return this;
    }

    /**
     * 是否让框架支持自定义 {@link Fragment} 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
     *
     * @param customFragment {@code true} 为支持
     */
    public DensityConfig setCustomFragment(boolean customFragment) {
        isCustomFragment = customFragment;
        return this;
    }

    /**
     * 框架是否已经开启支持自定义 {@link Fragment} 的适配参数
     *
     * @return {@code true} 为支持
     */
    public boolean isCustomFragment() {
        return isCustomFragment;
    }

    /**
     * 框架是否已经停止运行
     *
     * @return {@code false} 框架正在运行, {@code true} 框架已经停止运行
     */
    public boolean isStop() {
        return isStop;
    }

    /**
     * {@link ExternalAdapterManager} 用来管理外部三方库 {@link Activity} 的适配
     *
     * @return {@link ExternalAdapterManager}
     */
    public ExternalAdapterManager getExternalAdaptManager() {
        return mExternalAdaptManager;
    }

    /**
     * 返回 {@link #isBaseOnWidth}
     *
     * @return {@link #isBaseOnWidth}
     */
    public boolean isBaseOnWidth() {
        return isBaseOnWidth;
    }

    /**
     * 返回 {@link #isUseDeviceSize}
     *
     * @return {@link #isUseDeviceSize}
     */
    public boolean isUseDeviceSize() {
        return isUseDeviceSize;
    }

    /**
     * 返回 {@link #mScreenWidth}
     *
     * @return {@link #mScreenWidth}
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * 返回 {@link #mScreenHeight}
     *
     * @return {@link #mScreenHeight}
     */
    public int getScreenHeight() {
        return isUseDeviceSize() ? mScreenHeight : mScreenHeight - ScreenUtils.getStatusBarHeight() - ScreenUtils.getHeightOfNavigationBar(getApplication());
    }

    /**
     * 获取 {@link #mDesignWidthInDp}
     *
     * @return {@link #mDesignWidthInDp}
     */
    public int getDesignWidthInDp() {
        Preconditions.checkArgument(mDesignWidthInDp > 0, "you must set " + KEY_DESIGN_WIDTH_IN_DP + "  in your AndroidManifest file");
        return mDesignWidthInDp;
    }

    /**
     * 获取 {@link #mDesignHeightInDp}
     *
     * @return {@link #mDesignHeightInDp}
     */
    public int getDesignHeightInDp() {
        Preconditions.checkArgument(mDesignHeightInDp > 0, "you must set " + KEY_DESIGN_HEIGHT_IN_DP + "  in your AndroidManifest file");
        return mDesignHeightInDp;
    }

    /**
     * 获取 {@link #mInitDensity}
     *
     * @return {@link #mInitDensity}
     */
    public float getInitDensity() {
        return mInitDensity;
    }

    /**
     * 获取 {@link #mInitDensityDpi}
     *
     * @return {@link #mInitDensityDpi}
     */
    public int getInitDensityDpi() {
        return mInitDensityDpi;
    }

    /**
     * 获取 {@link #mInitScaledDensity}
     *
     * @return {@link #mInitScaledDensity}
     */
    public float getInitScaledDensity() {
        return mInitScaledDensity;
    }

    /**
     * 获取使用者在 AndroidManifest 中填写的 Meta 信息
     * <p>
     * Example usage:
     * <pre>
     * <meta-data android:name="design_width_in_dp"
     *            android:value="360"/>
     * <meta-data android:name="design_height_in_dp"
     *            android:value="640"/>
     * </pre>
     *
     * @param context {@link Context}
     */
    private void getMetaData(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo applicationInfo;
                try {
                    applicationInfo = packageManager.getApplicationInfo(context
                            .getPackageName(), PackageManager.GET_META_DATA);
                    if (applicationInfo != null && applicationInfo.metaData != null) {
                        if (applicationInfo.metaData.containsKey(KEY_DESIGN_WIDTH_IN_DP)) {
                            mDesignWidthInDp = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH_IN_DP);
                        }
                        if (applicationInfo.metaData.containsKey(KEY_DESIGN_HEIGHT_IN_DP)) {
                            mDesignHeightInDp = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT_IN_DP);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
