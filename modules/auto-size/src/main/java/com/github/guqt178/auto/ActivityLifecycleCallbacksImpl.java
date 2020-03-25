
package com.github.guqt178.auto;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * ================================================
 * {@link ActivityLifecycleCallbacksImpl} 可用来代替在 BaseActivity 中加入适配代码的传统方式
 * {@link ActivityLifecycleCallbacksImpl} 这种方案类似于 AOP, 面向接口, 侵入性低, 方便统一管理, 扩展性强, 并且也支持适配三方库的 {@link Activity}
 * <p>
 * ================================================
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {
    /**
     * 屏幕适配逻辑策略类
     */
    private AutoAdaptStrategy mAutoAdaptStrategy;
    /**
     * 让 {@link Fragment} 支持自定义适配参数
     */
    private FragmentLifecycleCallbacksImpl mFragmentLifecycleCallbacks;

    public ActivityLifecycleCallbacksImpl(AutoAdaptStrategy autoAdaptStrategy) {
        mFragmentLifecycleCallbacks = new FragmentLifecycleCallbacksImpl(autoAdaptStrategy);
        mAutoAdaptStrategy = autoAdaptStrategy;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (DensityConfig.getInstance().isCustomFragment()) {
            if (activity instanceof FragmentActivity) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks, true);
            }
        }

        //Activity 中的 setContentView(View) 一定要在 super.onCreate(Bundle); 之后执行
        if (mAutoAdaptStrategy != null) {
            mAutoAdaptStrategy.applyAdapt(activity, activity);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 设置屏幕适配逻辑策略类
     *
     * @param autoAdaptStrategy {@link AutoAdaptStrategy}
     */
    public void setAutoAdaptStrategy(AutoAdaptStrategy autoAdaptStrategy) {
        mAutoAdaptStrategy = autoAdaptStrategy;
        mFragmentLifecycleCallbacks.setAutoAdaptStrategy(autoAdaptStrategy);
    }
}
