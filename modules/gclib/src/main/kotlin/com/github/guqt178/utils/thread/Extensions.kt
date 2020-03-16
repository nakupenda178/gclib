package com.github.guqt178.utils.thread

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * 新建一个线程运行任务
 */
fun newThread(t: () -> Unit) {
    val namedThreadFactory = ThreadFactoryBuilder()
            .setNameFormat("my-thread-pool-%d").build()
    val singleThreadPool = ThreadPoolExecutor(
            1, 1,
            0L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(1024), namedThreadFactory, ThreadPoolExecutor.AbortPolicy()
    )
    singleThreadPool.execute { t() }
    singleThreadPool.shutdown()
}
