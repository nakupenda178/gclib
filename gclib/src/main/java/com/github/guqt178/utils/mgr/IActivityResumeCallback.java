package com.github.guqt178.utils.mgr;

import android.app.Activity;

/**
 * Activity onResume 事件回调接口
 */
public interface IActivityResumeCallback {

    /**
     * Activity onResume回调
     * @param activity 发生 onResume 事件的activity
     */
    void onActivityResume(Activity activity);
}
