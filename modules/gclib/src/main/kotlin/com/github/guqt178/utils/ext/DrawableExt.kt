package com.github.guqt178.utils.ext

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.v4.content.ContextCompat


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
