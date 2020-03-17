package com.github.guqt178.utils.dialog

import android.app.Activity
import com.github.guqt178.widgets.dialog.DialogMaker


/**
 *
 */
fun Activity?.showLoading(msg: String, cancelable: Boolean = true) {
    if (this != null && !DialogMaker.isShowing() && !this?.isDestroyed) {
        DialogMaker.showProgressDialog(this, msg, cancelable)
    }
}

fun Activity?.updateLoadingMessage(msg: String) {
    if (this != null && !this.isDestroyed) {
        DialogMaker.updateLoadingMessage(msg)
    }
}

fun Activity?.hideLoading() {
    if (this != null) {
        DialogMaker.dismissProgressDialog()
    }
}