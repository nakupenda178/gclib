package com.github.guqt178.utils.view

import android.view.View


fun View?.onClick(onClickListener: View.OnClickListener) {
    this?.setOnClickListener(onClickListener)
}


/*
 * 传入一个lambda表达式来进行扩展(只需要关心你执行的方法)
 */
fun View?.onClick(method: () -> Unit) {
    this?.setOnClickListener { method() }
}

