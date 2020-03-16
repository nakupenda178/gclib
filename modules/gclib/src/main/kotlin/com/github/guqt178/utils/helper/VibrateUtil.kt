package com.github.guqt178.utils.helper

import android.app.Service
import android.content.Context
import android.os.Vibrator


object VibrateUtil {

    //震动milliseconds毫秒
    private fun vibrateOneShot(context: Context, milliseconds: Long = 300) {
        getVibrateService(context).let {
            if (it.hasVibrator()) it.vibrate(milliseconds)
        }
    }

    //以pattern[]方式震动
    private fun vibrate(context: Context, pattern: LongArray = longArrayOf(100, 100, 100), repeat: Int = -1) {
        getVibrateService(context).let {
            if (it.hasVibrator()) it.vibrate(pattern, repeat)
        }
    }

    //取消震动
    private fun virateCancel(context: Context) {
        getVibrateService(context).let {
            if (it.hasVibrator()) it.cancel()
        }
    }


    /**
     * 播放振动和提示音
     * @param ring 是否播放提醒音
     */
    fun vibrateAndPlayRing(context: Context, ring: Boolean = true) {
        vibrateOneShot(context)
        if (ring) SoundPoolHelper.play(context)
    }

    private fun getVibrateService(context: Context) = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
}