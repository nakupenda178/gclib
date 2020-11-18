package com.github.guqt178.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AppleFragment extends Fragment {

    public static final int RESULT_OK = Activity.RESULT_OK;
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;

    private static final int REQUEST_CODE_INVALID = AppleActivity.REQUEST_CODE_INVALID;


    /**
     * Create a new instance of a Fragment with the given class name.
     * This is the same as calling its empty constructor.
     *
     * @param fragmentClass class of fragment.
     * @param <T>           subclass of {@link AppleFragment}.
     * @return new instance.
     */
    public final <T extends AppleFragment> T fragment(Class<T> fragmentClass) {
        return (T) instantiate(getContext(), fragmentClass.getName(), null);
    }

    /**
     * Create a new instance of a Fragment with the given class name.  This is the same as calling its empty constructor.
     *
     * @param fragmentClass class of fragment.
     * @param bundle        argument.
     * @param <T>           subclass of {@link AppleFragment}.
     * @return new instance.
     */
    public final <T extends AppleFragment> T fragment(Class<T> fragmentClass, Bundle bundle) {
        return (T) instantiate(getContext(), fragmentClass.getName(), bundle);
    }


    /**
     * AppleActivity.
     */
    private AppleActivity mActivity;

    /**
     * Get BaseActivity.
     *
     * @return {@link AppleActivity}.
     */
    protected final AppleActivity getCompatActivity() {
        return mActivity;
    }

    /**
     * Start activity.
     *
     * @param clazz class for activity.
     * @param <T>   {@link Activity}.
     */
    protected final <T extends Activity> void startActivity(Class<T> clazz) {
        startActivity(new Intent(mActivity, clazz));
    }

    /**
     * Start activity and finish my parent.
     *
     * @param clazz class for activity.
     * @param <T>   {@link Activity}.
     */
    protected final <T extends Activity> void startActivityFinish(Class<T> clazz) {
        startActivity(new Intent(mActivity, clazz));
        mActivity.finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppleActivity) context;
    }

    /**
     * Destroy me.
     */
    public void finish() {
        mActivity.onBackPressed();
    }



    // ------------------------- Stack ------------------------- //

    /**
     * Stack info.
     */
    private AppleActivity.FragmentStackEntity mStackEntity;

    /**
     * Set result.
     *
     * @param resultCode result code, one of {@link AppleFragment#RESULT_OK}, {@link AppleFragment#RESULT_CANCELED}.
     */
    protected final void setResult(@ResultCode int resultCode) {
        mStackEntity.resultCode = resultCode;
    }

    /**
     * Set result.
     *
     * @param resultCode resultCode, use {@link }.
     * @param result     {@link Bundle}.
     */
    protected final void setResult(@ResultCode int resultCode, @NonNull Bundle result) {
        mStackEntity.resultCode = resultCode;
        mStackEntity.result = result;
    }

    /**
     * Get the resultCode for requestCode.
     */
    final void setStackEntity(@NonNull AppleActivity.FragmentStackEntity stackEntity) {
        this.mStackEntity = stackEntity;
    }

    /**
     * You should override it.
     *
     * @param resultCode resultCode.
     * @param result     {@link Bundle}.
     */
    public void onFragmentResult(int requestCode, @ResultCode int resultCode, @Nullable Bundle result) {

    }

    // <editor-fold defaultstate="collapsed" desc="start方法">

    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     * @param <T>   {@link AppleFragment}.
     */
    public final <T extends AppleFragment> void startFragment(Class<T> clazz) {
        try {
            AppleFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
     * @param stickyStack sticky to back stack.
     * @param <T>         {@link AppleFragment}.
     */
    public final <T extends AppleFragment> void startFragment(Class<T> clazz, boolean stickyStack) {
        try {
            AppleFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, stickyStack, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param <T>            {@link AppleFragment}.
     */
    public final <T extends AppleFragment> void startFragment(T targetFragment) {
        startFragment(targetFragment, true, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param <T>            {@link AppleFragment}.
     */
    public final <T extends AppleFragment> void startFragment(T targetFragment, boolean stickyStack) {
        startFragment(targetFragment, stickyStack, REQUEST_CODE_INVALID);
    }


    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link AppleFragment}.
     */
    public final <T extends AppleFragment> void startFragmentForResult(Class<T> clazz, int requestCode) {
        try {
            AppleFragment targetFragment = clazz.newInstance();
            startFragment(targetFragment, true, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
     * @param <T>            {@link AppleFragment}.
     */
    public final <T extends AppleFragment> void startFragmentForResult(T targetFragment, int requestCode) {
        startFragment(targetFragment, true, requestCode);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param requestCode    requestCode.
     * @param <T>            {@link AppleFragment}.
     */
    private <T extends AppleFragment> void startFragment(T targetFragment, boolean stickyStack, int requestCode) {
        mActivity.startFragment(this, targetFragment, stickyStack, requestCode);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="生命周期方法">

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);


    }

    @LayoutRes
    public abstract int layoutId(); //fragment 布局文件id

    public void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }  //可选

    public void initData(@Nullable Bundle savedInstanceState) {
    } //可选


    // </editor-fold>

}
