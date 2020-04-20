package com.github.guqt178.utils.ext

import java.text.SimpleDateFormat
import java.util.*

fun Number.toDate(pattern: String = "yyyy-MM-dd",
                  split: String = "-"): String {
    val sdf = SimpleDateFormat(pattern, Locale.CHINA)
    return try {
        sdf.format(this)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        "--"
    }
}