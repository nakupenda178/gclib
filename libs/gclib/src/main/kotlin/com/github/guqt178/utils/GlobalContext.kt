package com.github.guqt178.utils

import android.app.Application
import android.content.Context

/**
 * 快捷方式获取application,建议在Application中初始化
 *
 */
class GlobalContext {

    companion object {
        private var context: Application? = null

        @JvmStatic
        fun init(app: Application) {
            context = app
        }

        fun getContext() = context
                ?: throw IllegalAccessException("invoke init in application first!!")
    }
}