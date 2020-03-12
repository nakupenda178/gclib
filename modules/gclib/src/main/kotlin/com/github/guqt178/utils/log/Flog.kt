package com.github.guqt178.utils.log

import android.util.Log

/**
 * 日志打印类，对打印日志进行封装，方便根据日志定位问题
 * 这个类会打印调用路径
 */
object Flog {
    private const val START_STACK_INDEX = 4
    private const val PRINT_STACK_COUTN = 2
    private var logCallback: IFLogLogCallback? = null
    fun setFLogLogCallback(callback: IFLogLogCallback?) {
        logCallback = callback
    }

    fun d(log: String?) {
        val sb = StringBuilder()
        appendStack(sb)
        sb.append(log)
        if (logCallback != null) {
            logCallback!!.logD("FLog", sb.toString())
        } else {
            Log.d("FLog", sb.toString())
        }
    }

    fun v(log: String?) {
        val sb = StringBuilder()
        appendStack(sb)
        sb.append(log)
        if (logCallback != null) {
            logCallback!!.logV("FLog", sb.toString())
        } else {
            Log.v("FLog", sb.toString())
        }
    }

    fun i(log: String?) {
        val sb = StringBuilder()
        appendStack(sb)
        sb.append(log)
        if (logCallback != null) {
            logCallback!!.logI("FLog", sb.toString())
        } else {
            Log.i("FLog", sb.toString())
        }
    }

    fun w(log: String?) {
        val sb = StringBuilder()
        appendStack(sb)
        sb.append(log)
        if (logCallback != null) {
            logCallback!!.logW("FLog", sb.toString())
        } else {
            Log.w("FLog", sb.toString())
        }
    }

    fun e(log: String?) {
        val sb = StringBuilder()
        appendStack(sb)
        sb.append(log)
        if (logCallback != null) {
            logCallback!!.logE("FLog", sb.toString())
        } else {
            Log.e("FLog", sb.toString())
        }
    }

    private fun appendStack(sb: StringBuilder) {
        val stacks =
            Thread.currentThread().stackTrace
        if (stacks != null && stacks.size > START_STACK_INDEX) {
            val lastIndex =
                Math.min(stacks.size - 1, START_STACK_INDEX + PRINT_STACK_COUTN)
            for (i in lastIndex downTo START_STACK_INDEX) {
                if (stacks[i] == null) {
                    continue
                }
                var fileName = stacks[i]!!.fileName
                if (fileName != null) {
                    val dotIndx = fileName.indexOf('.')
                    if (dotIndx > 0) {
                        fileName = fileName.substring(0, dotIndx)
                    }
                }
                sb.append(fileName)
                sb.append('(')
                sb.append(stacks[i]!!.lineNumber)
                sb.append(")")
                sb.append("->")
            }
            sb.append(stacks[START_STACK_INDEX]!!.methodName)
        }
        sb.append('\n')
    }

    /**
     * 日志回调，将要打印的日志回调给开发者，由开发者将日志输出
     */
    interface IFLogLogCallback {
        fun logD(tag: String?, log: String?)
        fun logV(tag: String?, log: String?)
        fun logI(tag: String?, log: String?)
        fun logW(tag: String?, log: String?)
        fun logE(tag: String?, log: String?)
    }
}