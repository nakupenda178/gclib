package com.github.guqt178.utils

import android.app.Application
import android.content.Context

/**
 * 快捷方式获取application
 *
 */
class GlobalContext {

    companion object{
        lateinit var context: Application

        @JvmStatic
        fun init(app: Application){
            context = app
        }
    }
}