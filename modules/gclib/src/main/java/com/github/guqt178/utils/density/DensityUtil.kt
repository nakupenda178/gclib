package com.github.guqt178.utils.density

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager

object DensityUtil {

    fun dip2px(context: Context, dipValue: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dipValue * density + 0.5f).toInt()
    }

    fun dip2px(dipValue: Float): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dipValue * density + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val density = context.resources.displayMetrics.density
        return (pxValue / density + 0.5f).toInt()
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val metrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val metrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId)
        }
        return result
    }
}
