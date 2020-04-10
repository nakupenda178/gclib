package com.github.guqt178.utils.dialog

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.alert.dialog.ProgressHelper
import com.github.guqt178.alert.dialog.SweetAlertDialog
import com.github.guqt178.dialogs.easy.DialogMaker


// <editor-fold defaultstate="collapsed" desc="简单的转圈dialog">

fun Activity?.showLoading(msg: String, cancelable: Boolean = false) {
    if (this != null && !this.isFinishing) {
        if (DialogMaker.isShowing())
            updateLoadingMessage(msg)
        else
            DialogMaker.showProgressDialog(this,0.0f, msg, cancelable)
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
    (this as? Activity)?.showLoading(msg, cancelable)
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

// </editor-fold>
