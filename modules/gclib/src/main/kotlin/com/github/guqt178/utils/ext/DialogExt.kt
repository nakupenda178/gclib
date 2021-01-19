package com.github.guqt178.utils.ext

import android.app.ProgressDialog
import android.content.Context
import android.support.v4.content.ContextCompat
import com.github.guqt178.R

// <editor-fold defaultstate="collapsed" desc="AlertDialog">
fun Context.showSystemDialog(cancelable: Boolean = false, msg: String = "请稍后...") =
        ProgressDialog.show(this, "", msg, false, false).also {
            it.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress_ios_loading))
        }!!

// </editor-fold>
