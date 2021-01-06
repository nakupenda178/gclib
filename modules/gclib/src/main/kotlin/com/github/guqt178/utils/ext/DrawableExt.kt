package com.github.guqt178.utils.ext

import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v4.content.ContextCompat
import java.util.*


/**
 * 根据原图,创建一个alpha为原图alphaRatio百分比的图,生成selector
 */
fun Context.generatePressedSelector(pressed: Int, alphaRatio: Float = 0.6f): Drawable {
    val stateDrawable = StateListDrawable()
    val originalDrawable = ContextCompat.getDrawable(this, pressed)
    //复制一张图并重新设置alpha值
    val copyDrawable = originalDrawable?.constantState?.newDrawable()?.also {
        it.mutate().alpha = it.alpha.times(alphaRatio).toInt()
    }
    //  按下状态设置按下的图片
    stateDrawable.addState(intArrayOf(R.attr.state_pressed), copyDrawable)
    //默认状态,默认状态下的图片
    stateDrawable.addState(intArrayOf(), originalDrawable)
    return stateDrawable
}


/*
* 图片选择器
* */
fun Context?.createStateListDrawable(normalResId: Int, pressedResId: Int): StateListDrawable? {
    this?.let {
        val result = StateListDrawable()
        result.addState(intArrayOf(android.R.attr.state_pressed), ContextCompat.getDrawable(it, pressedResId))
        result.addState(intArrayOf(android.R.attr.state_focused), ContextCompat.getDrawable(it, pressedResId))
        result.addState(intArrayOf(), ContextCompat.getDrawable(it, normalResId))
        return result
    }
    return null
}

/*
* 随机shape背景(solid shape)
* */
fun Context?.createGradientDrawable(backgroundColor: Int = -1, random: Boolean = true): GradientDrawable? {
    this?.let {
        val seed = Random()
        val r = seed.nextInt(255)
        val g = seed.nextInt(255)
        val b = seed.nextInt(255)

        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = it.dip(10).toFloat()
            setColor(if (random) Color.rgb(r, g, b) else backgroundColor)
        }
    }
    return null
}

/*stroke shape*/
fun Context?.createRoundRectShape(strokeColor: Int, corner: Int = 2, strokeWidth: Int = 1): GradientDrawable? {
    this?.let {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = it.dip(corner).toFloat()
            setStroke(it.dip(strokeWidth), strokeColor)
        }
    }
    return null
}

