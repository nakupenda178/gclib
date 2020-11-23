package com.github.guqt178.utils

import android.view.View
import com.github.guqt178.utils.anim.goneFade
import com.github.guqt178.utils.anim.showFade

/**
 * 淡入淡出动画
 */
class CrossFadeAnimation(private val showTarget: View, //要显示的view
                         private val hideTarget: View, //要gone掉的view
                         private val duration: Number = 600L/*淡入淡出动画时长*/) {


    fun apply() {
        showTarget.showFade(duration)

        hideTarget.goneFade(duration)
    }
}