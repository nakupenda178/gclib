package com.github.guqt178.dialogs.util;

import android.content.Context;
import android.support.annotation.Nullable;

import com.github.guqt178.dialogs.HintDialog;
import com.github.guqt178.dialogs.base.BaseDialog;

class DialogUtil {

    // <editor-fold defaultstate="collapsed" desc="带图标和按钮的提示框">

    public static void showHint(
            Context context,
            int type,
            @Nullable String content,
            Number duration,
            @Nullable BaseDialog.OnDismissListener dismissAction) {

        new HintDialog.Builder(context)
                .setType(type)
                .setMessage(content)
                .setOnDismissListener(dismissAction)
                .show(duration.longValue());
    }

    // </editor-fold>

}
