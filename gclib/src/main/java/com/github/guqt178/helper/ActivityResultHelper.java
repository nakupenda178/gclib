package com.github.guqt178.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 借助fragment优雅地申请权限和处理 onActivityResult
 * @see RouterFragment
 * @see "https://blog.csdn.net/gdutxiaoxu/article/details/86498647"
 */
public class ActivityResultHelper {

    private static final String TAG = "ActivityLauncher";
    private Context mContext;
    private RouterFragment mRouterFragment;

    public static ActivityResultHelper init(FragmentActivity activity) {
        return new ActivityResultHelper(activity);
    }

    private ActivityResultHelper(FragmentActivity activity) {
        mContext = activity;
        mRouterFragment = getRouterFragment(activity);
    }

    private RouterFragment getRouterFragment(FragmentActivity activity) {
        RouterFragment routerFragment = findRouterFragment(activity);
        if (routerFragment == null) {
            routerFragment = RouterFragment.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(routerFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return routerFragment;
    }

    private RouterFragment findRouterFragment(FragmentActivity activity) {
        return (RouterFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
    }

    public void startActivityForResult(Class<?> clazz, OnResult callback) {
        Intent intent = new Intent(mContext, clazz);
        startActivityForResult(intent, callback);
    }

    public void startActivityForResult(Intent intent, OnResult callback) {
        mRouterFragment.startActivityForResult(intent, callback);
    }

}