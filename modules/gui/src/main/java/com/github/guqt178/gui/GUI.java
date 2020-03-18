package com.github.guqt178.gui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.github.guqt178.gui.logs.UILog;

import java.lang.reflect.InvocationTargetException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * UI全局设置
 *
 * @author xuexiang
 * @since 2018/11/14 上午11:40
 */
public class GUI {

    private static volatile GUI sInstance = null;

    private static Application sContext;

    private static boolean sIsTabletChecked;

    private static int sScreenType;

    private GUI() {

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static GUI getInstance() {
        if (sInstance == null) {
            synchronized (GUI.class) {
                if (sInstance == null) {
                    sInstance = new GUI();
                }
            }
        }
        return sInstance;
    }

    //=======================初始化设置===========================//
    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Application context) {
        sContext = context;
    }

    /**
     * 设置默认字体
     */
    public GUI initFontStyle(String defaultFontAssetPath) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(defaultFontAssetPath)
                .setFontAttrId(R.attr.fontPath)
                .build());
        return this;
    }

    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    private static void testInitialize() {
        if (sContext == null) {
            init(getApplicationByReflect());
        }
    }


    @SuppressLint("PrivateApi")
    private static Application getApplicationByReflect() {
        try {
            Class activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new ExceptionInInitializerError("invoke GUI.init() first！");
            }
            return (Application)app;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch ( IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e ) {
            e.printStackTrace();
        }
        throw new ExceptionInInitializerError("invoke GUI.init() first！");
    }

    //=======================日志调试===========================//
    /**
     * 设置调试模式
     *
     * @param tag
     * @return
     */
    public static void debug(String tag) {
        UILog.debug(tag);
    }

    /**
     * 设置调试模式
     *
     * @param isDebug
     * @return
     */
    public static void debug(boolean isDebug) {
        UILog.debug(isDebug);
    }

    //=======================字体===========================//
    /**
     * @return 获取默认字体
     */
    @Nullable
    public static Typeface getDefaultTypeface() {
        String fontPath = CalligraphyConfig.get().getFontPath();
        if (!TextUtils.isEmpty(fontPath)) {
            return TypefaceUtils.load(getContext().getAssets(), fontPath);
        }
        return null;
    }

    /**
     * @param fontPath 字体路径
     * @return 获取默认字体
     */
    @Nullable
    public static Typeface getDefaultTypeface(String fontPath) {
        if (TextUtils.isEmpty(fontPath)) {
            fontPath = CalligraphyConfig.get().getFontPath();
        }
        if (!TextUtils.isEmpty(fontPath)) {
            return TypefaceUtils.load(getContext().getAssets(), fontPath);
        }
        return null;
    }

    //=======================屏幕尺寸===========================//

    /**
     * 检验设备屏幕的尺寸
     * @param context
     * @return
     */
    private static int checkScreenSize(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            //证明是平板
            if (screenSize >= Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                return UIConsts.ScreenType.BIG_TABLET;
            } else {
                return UIConsts.ScreenType.SMALL_TABLET;
            }
        } else {
            return UIConsts.ScreenType.PHONE;
        }
    }

    /**
     * 判断是否平板设备
     * @return true:平板,false:手机
     */
    public static int getScreenType() {
        if (sIsTabletChecked) {
            return sScreenType;
        }
        sScreenType = checkScreenSize(GUI.getContext());
        sIsTabletChecked = true;
        return sScreenType;
    }

    /**
     * 是否是平板
     * @return
     */
    public static boolean isTablet() {
        return getScreenType() == UIConsts.ScreenType.SMALL_TABLET || getScreenType() == UIConsts.ScreenType.BIG_TABLET;
    }

    /**
     * 初始化主题
     * @param activity
     */
    public static void initTheme(Activity activity) {
        int screenType = getScreenType();
        if (screenType == UIConsts.ScreenType.PHONE) {
            activity.setTheme(R.style.XUITheme_Phone);
        } else if (screenType == UIConsts.ScreenType.SMALL_TABLET){
            activity.setTheme(R.style.XUITheme_Tablet_Small);
        } else {
            activity.setTheme(R.style.XUITheme_Tablet_Big);
        }
    }

}
