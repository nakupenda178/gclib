package com.github.guqt178.utils.keyboard

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*


/*打开软键盘*/
inline fun Context.openKeyboard(view: View) {
    with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
        showSoftInput(view, InputMethodManager.RESULT_SHOWN)
        toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

/*延时打开软键盘*/
fun Context.openKeyboardDelay(view: View, delay: Long = 200L) {
    Timer().schedule(object : TimerTask() {
        override fun run() {
            with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
                showSoftInput(view, 0)
            }
        }
    }, delay)
}

/*关闭软键盘*/
fun Context.closeKeyboard(view: View) {
    with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}