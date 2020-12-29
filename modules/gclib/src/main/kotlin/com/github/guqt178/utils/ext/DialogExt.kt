package com.github.guqt178.utils.ext

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.R
import com.github.guqt178.alert.dialog.ProgressHelper
import com.github.guqt178.alert.dialog.SweetAlertDialog
import com.github.guqt178.dialogs.easy.DialogMaker
import com.github.guqt178.utils.thread.postDelay


// <editor-fold defaultstate="collapsed" desc="简单的转圈dialog">

fun Activity?.showLoading(msg: String,
                          dismissDelayTime: Number = 0,
                          cancelable: Boolean = false) {
    if (this != null && !this.isFinishing) {
        if (DialogMaker.isShowing())
            updateLoadingMessage(msg)
        else
            DialogMaker.showProgressDialog(this, 0.0f, msg, cancelable)

        if(dismissDelayTime.toInt() >0){
            postDelay(dismissDelayTime.toLong()) {
                hideLoading()
            }
        }
    }
}

internal fun Activity?.updateLoadingMessage(msg: String) {
    if (this != null && !this.isFinishing) {
        DialogMaker.updateLoadingMessage(msg)
    }
}

fun Activity?.hideLoading() {
    if (this != null) {
        DialogMaker.dismissProgressDialog()
    }
}

fun Fragment?.showLoading(msg: String, cancelable: Boolean = true) {
    this?.activity?.showLoading(msg, cancelable)
}

fun Fragment?.updateLoadingMessage(msg: String) {
    this?.activity?.updateLoadingMessage(msg)
}

fun Fragment?.hideLoading() {
    this?.activity?.hideLoading()
}

fun Context?.showLoading(msg: String, cancelable: Boolean = true) {
    (this as? Activity)?.showLoading(msg,0, cancelable)
}

fun Context?.updateLoadingMessage(msg: String) {
    (this as? Activity)?.updateLoadingMessage(msg)
}

fun Context?.hideLoading() {
    (this as? Activity)?.hideLoading()
}

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="带图标和按钮的提示框">
fun Context.showSuccessTip(title: String,
                           content: String,
                           config: DefaultConsumer<ProgressHelper>? = null): SweetAlertDialog {
    val dialog = SweetAlertDialog(this).also {
        config?.invoke(it.progressHelper)
        it.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        it.titleText = title
        it.contentText = content
    }
    dialog.show()
    return dialog
}
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="AlertDialog">
fun Context.showSystemDialog(cancelable: Boolean = false, msg: String = "请稍后...") =
        ProgressDialog.show(this, "", msg, false, false).also {
            it.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress_ios_loading))
        }!!

// </editor-fold>
