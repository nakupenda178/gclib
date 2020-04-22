package com.github.guqt178.utils.ext

fun String?.ifEmpty(option: String = "") = if (this.isNullOrEmpty()) option else this
