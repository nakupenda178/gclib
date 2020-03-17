package com.github.guqt178.utils.thread

import android.os.Handler
import android.os.Looper
import com.github.guqt178.DefaultAction
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.DefaultSupplier
import com.github.guqt178.utils.GlobalContext

internal val H = Handler(Looper.getMainLooper())

/**
 * 异步运行一个任务
 */
fun <R> Any.doAsyncTask(backgroundAction: DefaultSupplier<R>,
                    onResult: DefaultConsumer<R>? = null): GlobalContext.Task<R>? {
    return GlobalContext.doAsync(object : GlobalContext.Task<R>(object : GlobalContext.Callback<R> {
        override fun onCall(data: R) {
            onResult?.invoke(data)
        }
    }) {
        override fun doInBackground(): R {
            try {
                return backgroundAction.invoke()
            } catch (e: Exception) {
                throw IllegalStateException("doInBackground occur exception!")
            }
        }
    })
}

/**
 * 执行延迟任务,在主线程
 */
fun Any.postDelay(delay: Number = 1000, action: DefaultAction) {
    H.postDelayed({ action.invoke() }, delay.toLong())
}