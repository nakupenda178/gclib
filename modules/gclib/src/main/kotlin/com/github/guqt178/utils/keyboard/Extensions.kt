package com.github.guqt178.utils.keyboard

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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

/**
 * 点击空白区域隐藏键盘
 */
fun Activity.hideKeyboard(event: MotionEvent) {
    try {
        val focusView = this.currentFocus
        if (focusView != null && focusView is EditText) {

            //val region = Rect()
            //focusView.getHitRect(region)
            //err("x,y is in rect?${region.contains(event.x.toInt(), event.y.toInt())}")
            //focusView.getGlobalVisibleRect()
            val location = intArrayOf(0, 0)
            focusView.getLocationInWindow(location)

            val left = location[0]
            val top = location[1]
            val right = left + focusView.width
            val bottom = top + focusView.height
            // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
            if (event.rawX < left || event.rawX > right
                || event.y < top || event.rawY > bottom) {
                // 隐藏键盘
                val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
