package com.github.guqt178.utils.ext

import android.view.View
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.utils.ClickUtils

fun View?.onClick(onClickListener: View.OnClickListener) {
    this?.doOnClick{
        onClickListener.onClick(it)
    }
}

fun View?.doOnClick(action: DefaultConsumer<View>) {
    this?.onDebounceClick(1000, action = action)
}

fun View?.onDebounceClick(duration: Long = 2000,
                          action: DefaultConsumer<View>) {
    if (this == null) return
    ClickUtils.applyGlobalDebouncing(this, duration) {
        action.invoke(this)
    }
}



