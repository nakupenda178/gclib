package com.github.guqt178.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.Random;

/**
 * 无界面fragment
 * 优雅地申请权限和处理 onActivityResult
 * 不需要重写Activity的onActivityResult
 *
 */
public class RouterFragment extends Fragment {

    private SparseArray<OnResult> mCallbacks = new SparseArray<>();
    private Random mCodeGenerator = new Random();

    public RouterFragment() {}


    public static RouterFragment newInstance() {
        return new RouterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startActivityForResult(Intent intent, OnResult callback) {
        int requestCode = makeRequestCode();
        mCallbacks.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    private int makeRequestCode() {
        int requestCode;
        int tryCount = 0;
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF);
            tryCount++;
        } while (mCallbacks.indexOfKey(requestCode) >= 0 && tryCount < 10);
        return requestCode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OnResult callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);
        if (callback != null) {
            callback.onActivityResult(requestCode,resultCode, data);
        }
    }
}