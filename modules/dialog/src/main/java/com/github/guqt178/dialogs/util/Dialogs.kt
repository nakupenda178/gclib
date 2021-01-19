package com.github.guqt178.dialogs.util


import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.github.guqt178.dialogs.HintDialog
import com.github.guqt178.dialogs.R
import com.github.guqt178.dialogs.base.BaseDialog
import com.github.guqt178.dialogs.easy.DialogMaker


// <editor-fold defaultstate="collapsed" desc="简单的转圈dialog">
internal val IH = Handler(Looper.getMainLooper())
typealias Consumer<T> = (T) -> Unit

fun Activity?.showLoading(msg: String,
                          dismissDelayTime: Number = 0,
                          cancelable: Boolean = false) {
    if (this != null && !this.isFinishing) {
        if (DialogMaker.isShowing())
            updateLoadingMessage(msg)
        else
            DialogMaker.showProgressDialog(this, 0.0f, msg, cancelable)

        if (dismissDelayTime.toInt() > 0) {
            IH.postDelayed({ hideLoading() }, dismissDelayTime.toLong())
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
    (this as? Activity)?.showLoading(msg, 0, cancelable)
}

fun Context?.updateLoadingMessage(msg: String) {
    (this as? Activity)?.updateLoadingMessage(msg)
}

fun Context?.hideLoading() {
    (this as? Activity)?.hideLoading()
}

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="带图标和按钮的提示框">
fun Context.showHint(type: Int = 1,
                     content: String? = "",
                     duration: Number = 1000,
                     dismissAction: Consumer<BaseDialog>? = null) {

    HintDialog.Builder(this)
            .setType(type)
            .setMessage(content.orEmpty())
            .setOnDismissListener {
                dismissAction?.invoke(it)
            }
            .show(duration.toLong())

}
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="AlertDialog">
fun Context.showSystemDialog(cancelable: Boolean = false, msg: String = "请稍后...") =
        ProgressDialog.show(this, "", msg, false, false).also {
            it.setIndeterminateDrawable(ContextCompat.getDrawable(this, R.drawable.progress_ios_loading))
        }!!

// </editor-fold>
