package com.github.guqt178.utils.color

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.github.guqt178.utils.GlobalContext


fun String.toColor(): Int {
    try {
        return Color.parseColor(this)
    } catch (e: Exception) {
        throw IllegalArgumentException("[$this] is not legal color string!")
    }
}