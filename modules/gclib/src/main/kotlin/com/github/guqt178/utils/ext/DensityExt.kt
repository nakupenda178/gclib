package com.github.guqt178.utils.ext

import android.content.Context
import android.util.TypedValue


fun Context.px2dp(px: Number) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        px.toFloat(),
        this.resources.displayMetrics
)
