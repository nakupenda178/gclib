package com.github.guqt178.binding.viewadapter.view

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter(
    value = ["onClickCommand", "isInterval", "intervalMilliseconds"],
    requireAll = false
)
fun onClickCommand(
    view: View,
    clickCommand: View.OnClickListener,
    // xml中没有配置，那么使用全局的配置
    isInterval: Boolean = true,
    // 没有配置时间，使用全局配置
    intervalMilliseconds: Int = 1500
) {
    if (isInterval) {
        view.clickWithTrigger(intervalMilliseconds.toLong(), clickCommand)
    } else {
        view.setOnClickListener(clickCommand)
    }
}