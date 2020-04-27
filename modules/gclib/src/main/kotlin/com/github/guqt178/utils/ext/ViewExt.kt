package com.github.guqt178.utils.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.log.Alog
import com.github.guqt178.utils.ClickUtils

fun View?.onClick(onClickListener: View.OnClickListener) {
    this?.doOnClick {
        onClickListener.onClick(it)
    }
}

fun View?.doOnClick(action: DefaultConsumer<View>) {
    this?.onDebounceClick(1000, action = action)
}

//防重复点击
fun View?.onDebounceClick(duration: Long = 2000,
                          action: DefaultConsumer<View>) {
    if (this == null) return
    ClickUtils.applyGlobalDebouncing(this, duration) {
        action.invoke(this)
    }
}

//弹性点击事件(有按下和弹起的动画,已滤重)
var lastClickTime = 0L
fun View?.onFlexibleClick(action: DefaultConsumer<View>? = null) {
    this?.setOnTouchListener { view, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                view.pivotX = view.width / 2.toFloat()
                view.pivotY = view.height / 2.toFloat()
                val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.9f)
                val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.9f)
                val animatorSet = AnimatorSet()
                animatorSet.playTogether(scaleX, scaleY)
                animatorSet.duration = 100
                animatorSet.start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                view.setPivotX(view.getWidth() / 2.toFloat())
                view.setPivotY(view.getHeight() / 2.toFloat())
                val scaleX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.03f)
                val scaleY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.03f)
                val animatorSet2 = AnimatorSet()
                animatorSet2.playTogether(scaleX2, scaleY2)
                animatorSet2.duration = 100
                animatorSet2.start()
                animatorSet2.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        val scaleX3 = ObjectAnimator.ofFloat(view, "scaleX", 1.03f, 1f)
                        val scaleY3 = ObjectAnimator.ofFloat(view, "scaleY", 1.03f, 1f)
                        val animatorSet3 = AnimatorSet()
                        animatorSet3.playTogether(scaleX3, scaleY3)
                        animatorSet3.duration = 100
                        animatorSet3.start()
                        animatorSet3.addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                val now = System.currentTimeMillis()
                                if (now - lastClickTime > 2000L) {
                                    lastClickTime = now
                                    action?.invoke(view)
                                }
                            }
                        })
                    }
                })
            }
        }
        true
    }
}



