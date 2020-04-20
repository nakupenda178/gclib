package com.github.guqt178.utils.ext

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.utils.keyboard.keyboardvisibilityevent.AutoActivityLifecycleCallback
import com.github.guqt178.utils.keyboard.keyboardvisibilityevent.KeyboardVisibilityEvent
import com.github.guqt178.utils.keyboard.keyboardvisibilityevent.KeyboardVisibilityEventListener

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
            if (event.rawX < left || event.rawX > right || event.y < top || event.rawY > bottom) {
                // 隐藏键盘
                getInputMethodManager().hideSoftInputFromWindow(focusView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


/**
 * Show keyboard and focus to given EditText.
 * Use this method if target EditText is in Dialog.
 *
 * @param dialog Dialog
 * @param target EditText to focus
 */
fun Dialog.showKeyboardInDialog(target: EditText) {
    this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    target.requestFocus()
}

/**
 * hide keyboard
 *
 * @param context Context
 * @param target  View that currently has focus
 */
fun Context.hideKeyboard(target: View) {
    val imm = this.getInputMethodManager()
    imm.hideSoftInputFromWindow(target.windowToken, 0)
}

/**
 * hide keyboard
 *
 * @param activity Activity
 */
fun Activity.hideKeyboard() {
    val view = this.window.decorView
    this.hideKeyboard(view)
}

/**
 * Show keyboard and focus to given EditText
 *
 * @param context Context
 * @param target  EditText to focus
 */
fun Context.showKeyboard(target: EditText) {
    val imm = this.getInputMethodManager()
    imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Show keyboard
 *
 * @param context Context
 * @param target  EditText to focus
 */
fun Activity.showKeyboardForce(view: View? = null) {
    val target = view ?: this.findViewById(android.R.id.content)
    with(getInputMethodManager()) {
        showSoftInput(target, InputMethodManager.RESULT_SHOWN)
        toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

private fun Context.getInputMethodManager(): InputMethodManager {
    return this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}


/**
 * observe keyboard visibility event
 * @param listener true - keyboard is open,otherwise is closed
 */
fun Activity?.observeKeyboardEvent(listener: DefaultConsumer<Boolean>? = null) {
    this?.let {
        val unregistrar = KeyboardVisibilityEvent.registerEventListener(this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                listener?.invoke(isOpen)
            }
        })

        this.application
                .registerActivityLifecycleCallbacks(object : AutoActivityLifecycleCallback(this) {
                    override fun onTargetActivityDestroyed() {
                        unregistrar.unregister()
                    }
                })
    }
}