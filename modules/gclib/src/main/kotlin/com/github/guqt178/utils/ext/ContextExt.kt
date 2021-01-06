package com.github.guqt178.utils.ext

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.github.guqt178.DefaultAction
import com.github.guqt178.utils.AppStackManager
import com.github.guqt178.utils.GlobalContext
import com.github.guqt178.utils.StatusBarUtil
import kotlin.system.exitProcess


//returns dip(dp) dimension value in pixels
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

//return sp dimension value in pixels
fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()
fun Context.sp(value: Float): Int = (value * resources.displayMetrics.scaledDensity).toInt()

//converts px value into dip or sp
fun Context.px2dip(px: Int): Float = px.toFloat() / resources.displayMetrics.density
fun Context.px2sp(px: Int): Float = px.toFloat() / resources.displayMetrics.scaledDensity

fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)


//the same for the views
inline fun View.dip(value: Int): Int = context.dip(value)
inline fun View.dip(value: Float): Int = context.dip(value)
inline fun View.sp(value: Int): Int = context.sp(value)
inline fun View.sp(value: Float): Int = context.sp(value)
inline fun View.px2dip(px: Int): Float = context.px2dip(px)
inline fun View.px2sp(px: Int): Float = context.px2sp(px)
inline fun View.dimen(@DimenRes resource: Int): Int = context.dimen(resource)

//the same for Fragments
inline fun Fragment.dip(value: Int): Int = requireActivity().dip(value)
inline fun Fragment.dip(value: Float): Int = requireActivity().dip(value)
inline fun Fragment.sp(value: Int): Int = requireActivity().sp(value)
inline fun Fragment.sp(value: Float): Int = requireActivity().sp(value)
inline fun Fragment.px2dip(px: Int): Float = requireActivity().px2dip(px)
inline fun Fragment.px2sp(px: Int): Float = requireActivity().px2sp(px)
inline fun Fragment.dimen(@DimenRes resource: Int): Int = requireActivity().dimen(resource)

//
fun Context?.resolveColor(@ColorRes color: Int) = this?.let {
    ContextCompat.getColor(this, color)
}

fun Context.resolveColor(@ColorRes color: Int) = ContextCompat.getColor(this, color)

//全局context
val globalContext = GlobalContext.getContext()


//沉浸式
fun Activity?.transparency() = StatusBarUtil.transparencyBar(this)

//状态栏黑色
fun Activity?.lightMode() = StatusBarUtil.statusBarLightMode(this)

//沉浸式and...
fun Activity?.transparencyAndLightMode() {
    StatusBarUtil.transparencyBar(this)
    StatusBarUtil.statusBarLightMode(this)
}

fun Activity?.hideBottomBar() {
    //隐藏虚拟按键
    if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
        this?.window?.decorView?.systemUiVisibility = View.GONE
    } else if (Build.VERSION.SDK_INT >= 19) {
        //for new api versions.
        val decorView = this?.window?.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        /* | View.SYSTEM_UI_FLAG_FULLSCREEN*/;
        decorView?.systemUiVisibility = uiOptions

    }
}

//
//双击退出
var firstClickTime: Long = 0

@JvmOverloads
fun Activity.doubleClickToExit(duration: Long = 2000, onFail: DefaultAction? = null, onSuccess: DefaultAction? = null) {
    val currentTimeMillis = System.currentTimeMillis()
    if (currentTimeMillis - firstClickTime > duration) {
        onFail?.invoke()
        firstClickTime = currentTimeMillis
    } else {
        onSuccess?.invoke()
        AppStackManager.getInstance().finishAllActivity()
        exitProcess(0)
    }

}