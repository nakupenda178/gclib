package com.github.guqt178.utils.ext

import android.graphics.Color

fun String.toColor(): Int {
    try {
        return Color.parseColor(this)
    } catch (e: Exception) {
        throw IllegalArgumentException("[$this] is not legal color string!")
    }
}