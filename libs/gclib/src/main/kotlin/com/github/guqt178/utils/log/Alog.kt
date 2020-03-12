package com.github.guqt178.utils.log

import com.github.guqt178.utils.log.LoggerWrapper.Companion.logger

/**
 * xLog
 * com.elvishew.xlog.XLog
 */
object ALog {
    fun wtf(msg: String, vararg format: Any) {
        logger!!.wtf(msg, *format)
    }

    fun warn(msg: String, vararg format: Any) {
        logger!!.warn(msg, *format)
    }

    fun info(msg: String, vararg format: Any) {
        logger!!.info(msg, *format)
    }

    fun error(msg: String, vararg format: Any) {
        logger!!.error(msg, *format)
    }

    fun debug(msg: String, vararg format: Any) {
        logger!!.debug(msg, *format)
    }

    fun tag(tag: String) {
        logger!!.tag(tag)
    }

    val tag: String
        get() = logger!!.getTag()
}