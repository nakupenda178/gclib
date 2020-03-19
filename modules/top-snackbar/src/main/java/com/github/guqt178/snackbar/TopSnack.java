package com.github.guqt178.snackbar;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;


/**
 * <p>
 * TopSnackbar snackbar = TopSnackbar.make(findViewById(R.id.ll_root),
 * msg, MySnackbar.LENGTH_SHORT, Gravity.TOP);
 * //修改颜色
 * TopSnackbar.SnackbarLayout content = (TopSnackbar.SnackbarLayout) snackbar.getView();
 * content.setBackgroundColor(getResources().getColor(R.color.text_color_gold));
 * <p>
 * //修改高度
 * Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
 * ViewGroup.LayoutParams lp = content.getLayoutParams();
 * lp.height = toolbar.getHeight();
 * content.setLayoutParams(lp);
 * <p>
 * //内容偏下移一点
 * content.setPadding(0,70,0,0);
 * snackbar.show();
 * </p>
 */

public class TopSnack {

    private WeakReference<Activity> mActivityWeakRef;
    private TopSnackbar mSnakebar;


    private TopSnack(Activity activity, Params params) {
        mActivityWeakRef = new WeakReference<>(activity);
        if (getDectorView() != null)
            mSnakebar = TopSnackbar.make(getDectorView(), params.message, params.duration, params.layoutGravity);
        if (null != mSnakebar) {
            if (params.onActionClickListener != null)
                mSnakebar.setAction(params.action, params.onActionClickListener);

            if (params.mCallback != null)
                mSnakebar.setCallback(params.mCallback);

            if (params.actionColor != -1)
                mSnakebar.setActionTextColor(params.actionColor);

            TopSnackbar.SnackbarLayout content = (TopSnackbar.SnackbarLayout) mSnakebar.getView();
            if (params.backgroundColor != -1 && content != null) {
                content.setBackgroundColor(params.backgroundColor);
            }

            if (params.height != -1 && content != null) {
                ViewGroup.LayoutParams lp = content.getLayoutParams();
                lp.height = params.height;
                content.setLayoutParams(lp);
            }

            if (params.paddings != null && content != null) {
                content.setPadding(params.paddings[0], params.paddings[1], params.paddings[2], params.paddings[3]);
            }
        }
    }


    private Activity getActivity() {
        if (mActivityWeakRef != null && mActivityWeakRef.get() != null) {
            return mActivityWeakRef.get();
        } else {
            return null;
        }
    }


    /**
     * 显示
     */
    public void show() {
        if (mSnakebar != null && getActivity() != null) {
            mSnakebar.show();
        }
    }


    /**
     * 显示
     */
    public void setMessage(String text) {
        if (mSnakebar != null && getActivity() != null) {
            mSnakebar.setText(text);
        }
    }

    public static Builder builder(Activity activity) {
        return new Builder(activity);
    }

    private View getDectorView() {
        if (getActivity() != null) {
            return getActivity().getWindow().getDecorView();
        }
        return null;
    }

    public static final class Builder {
        private Activity context;
        private Params params = new Params();

        public static final int DURATION_SHORT = TopSnackbar.LENGTH_SHORT;
        public static final int DURATION_LONG = TopSnackbar.LENGTH_LONG;
        public static final int DURATION_INFINITE = TopSnackbar.LENGTH_INDEFINITE;

        @IntDef({DURATION_SHORT, DURATION_LONG, DURATION_INFINITE})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Duration {
        }

        private Builder(Activity activity) {
            this.context = activity;
        }


        public Builder setDuration(@Duration int duration) {
            params.duration = duration;
            return this;
        }

        public Builder setMessage(String msg) {
            params.message = msg;
            return this;
        }

        public Builder setGravity(int gravity) {
            params.layoutGravity = gravity;
            return this;
        }

        public Builder setCallback(TopSnackbar.Callback callback) {
            params.mCallback = callback;
            return this;
        }

        public Builder setAction(String action, View.OnClickListener listener) {
            params.action = action;
            params.onActionClickListener = listener;
            return this;
        }

        public Builder setActionColor(@ColorInt int color) {
            params.actionColor = color;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            params.backgroundColor = color;
            return this;
        }

        public Builder setHeight(int height) {
            params.height = height;
            return this;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            int[] temp = {left, top, right, bottom};
            params.paddings = temp;
            return this;
        }


        private TopSnack create() {
            return new TopSnack(context, params);
        }

        public TopSnack show() {
            final TopSnack snake = create();
            snake.show();
            return snake;
        }
    }


    final static class Params {

        /**
         * 标题
         */
        public String title;

        /**
         * 文字信息
         */
        public String message;

        /**
         * 按钮文字
         */
        public String action;

        /**
         * 按钮图片资源
         */
        public int actionIcon;

        /**
         * 按钮点击时间
         */
        public View.OnClickListener onActionClickListener;

        /**
         * 左侧图标资源
         */
        public int iconResId;

        /**
         * 背景颜色
         */
        public int backgroundColor = -1;

        /**
         * 高度
         */
        public int height = -1;

        /**
         * 标题文字颜色
         */
        public int titleColor;

        /**
         * 内容偏移量
         */
        public int[] paddings;

        /**
         * 提示信息文字颜色
         */
        public int messageColor;

        /**
         * 按钮文字颜色
         */
        public int actionColor = -1;

        /**
         * 消失显示回调
         */
        public TopSnackbar.Callback mCallback;

        /**
         * 显示持续时间
         */
        public int duration = TopSnackbar.LENGTH_SHORT;

        /**
         * 布局对齐方式
         */
        public int layoutGravity = Gravity.TOP;


    }

}
