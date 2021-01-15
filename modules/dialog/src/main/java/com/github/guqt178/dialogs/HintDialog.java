package com.github.guqt178.dialogs;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.github.guqt178.dialogs.base.BaseDialog;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/12/17
 * desc   : 显示几秒后自动消失
 */
public final class HintDialog {


    public static final class Builder
            extends BaseDialog.Builder<Builder> implements Runnable, BaseDialog.OnDismissListener {

        private AppCompatImageView mIcon;
        private AppCompatTextView mContent;
        private BaseDialog.OnDismissListener listener;

        public Builder(Context context) {
            super(context);
            setCancelable(false);
            setBackgroundDimAmount(0f);
            setContentView(R.layout.hint_dialog);

            mIcon = findViewById(R.id.iv_hint_icon);
            mContent = findViewById(R.id.tv_hint_message);

            addOnDismissListener(this);
        }

        @Override
        public BaseDialog create() {
            return super.create();
        }

        public Builder setType(@IntRange(from = 0, to = 2) int type) {
            int icon;
            switch (type) {
                case 0:
                    icon = R.drawable.warning_ic;
                    break;
                case 1:
                    icon = R.drawable.error_ic;
                    break;
                default:
                    icon = R.drawable.finish_ic;
            }
            if (mIcon != null) {
                mIcon.setImageResource(icon);
            }
            return this;
        }

        public Builder setMessage(@Nullable String message) {

            if (mContent == null) {
                return this;
            }

            if (TextUtils.isEmpty(message)) {
                mContent.setVisibility(View.GONE);
            } else {
                mContent.setText(message);
            }
            return this;
        }

        public Builder setOnDismissListener(BaseDialog.OnDismissListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public BaseDialog show() {
            return show(1000);
        }


        public BaseDialog show(long duration) {
            postDelayed(this, duration);
            return super.show();
        }


        @Override
        public void run() {
            dismiss();
        }

        @Override
        public void onDismiss(BaseDialog dialog) {

            if (this.listener != null) {
                this.listener.onDismiss(dialog);
            }
        }
    }


}