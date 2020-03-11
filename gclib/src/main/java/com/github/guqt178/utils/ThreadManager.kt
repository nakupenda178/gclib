package com.github.guqt178.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.support.annotation.IntDef
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * 基于HandlerThread的多线程处理分发类
 */
class ThreadManager private constructor() {

    private var mBgHandlerThread: HandlerThread? = null
    private var mTempHandlerThread: HandlerThread? = null
    private var mRequestHandlerThread: HandlerThread? = null

    private var mMainHandler: Handler? = null
    private var mPostDelayTempHandler: Handler? = null

    //临时线程池，用于通过Runnable处理其余临时逻辑
    private var executor: ExecutorService? = null

    private fun getLooper(@ThreadType type: Int): Looper {
        when (type) {
            LOOPER_TYPE_ANDROID_MAIN
            -> {
                if (null == mMainHandler) {
                    mMainHandler = Handler(Looper.getMainLooper())
                }
                return Looper.getMainLooper()
            }
            LOOPER_TYPE_BG
            -> {
                if (null == mBgHandlerThread) {
                    mBgHandlerThread = HandlerThread(THREAD_NAME_BG)
                    mBgHandlerThread!!.start()
                }
                return mBgHandlerThread!!.looper
            }
            LOOPER_TYPE_REQUEST
            -> {
                if (null == mRequestHandlerThread) {
                    mRequestHandlerThread = HandlerThread(THREAD_NAME_REQUEST)
                    mRequestHandlerThread!!.start()
                }
                return mRequestHandlerThread!!.looper
            }
            else
            -> {
                if (null == mTempHandlerThread) {
                    mTempHandlerThread = HandlerThread(THREAD_NAME_TEMP)
                    mTempHandlerThread!!.start()
                }
                return mTempHandlerThread!!.looper
            }
        }
    }

    private fun initExecutorService() {
        if (null == executor) {
            executor = try {
                Executors.newFixedThreadPool(MAX_RUNNING_THREAD, CommonThreadFactory())
            } catch (t: Throwable) {
                Executors.newCachedThreadPool(CommonThreadFactory())
            }

        }
    }

    /**
     * 发起网络请求
     * @param task
     * @param resultCallback 响应成功后的回调
     */
    fun <T> fetchDataFromServer(task: Callable<T>, resultCallback: ((T) -> Unit)? = null) {
        initExecutorService()
        //i("-----fetchDataFromServer---${Thread.currentThread()}")
        executor?.submit(task)?.get(10, TimeUnit.SECONDS)?.let { result ->
            runOnUiThread(Runnable {
                resultCallback?.invoke(result)
            }, 0)
        }
    }

    fun start(runnable: Runnable) {
        initExecutorService()
        try {
            executor!!.submit(runnable)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }


    /**
     * 在子线程执行动作
     */
    @JvmOverloads
    fun run(runnable: Runnable, seconds: Int = 1) {
        if (null == mPostDelayTempHandler) {
            mPostDelayTempHandler = Handler(getLooper(LOOPER_TYPE_TEMP))
        }
        mPostDelayTempHandler!!.postDelayed(runnable, (seconds * 1000).toLong())
    }


    @JvmOverloads
    fun runOnUiThread(runnable: Runnable, delay: Number = 0) {
        getLooper(LOOPER_TYPE_ANDROID_MAIN)
        mMainHandler?.postDelayed(runnable, delay.toLong())
    }


    private class CommonThreadFactory : ThreadFactory {
        private val poolNumber = AtomicInteger(1)
        private val group: ThreadGroup
        private val threadNumber = AtomicInteger(1)
        private val namePrefix: String

        init {
            val s = System.getSecurityManager()
            group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
            namePrefix = THREAD_NAME_REQUEST + "- pool-" + poolNumber.getAndIncrement()
        }

        override fun newThread(r: Runnable): Thread {
            val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
            if (t.isDaemon)
                t.isDaemon = false
            if (t.priority != Thread.MIN_PRIORITY)
                t.priority = Thread.MIN_PRIORITY
            return t
        }
    }


    companion object {

        const val LOOPER_TYPE_BG = 1
        const val LOOPER_TYPE_TEMP = 3
        const val LOOPER_TYPE_REQUEST = 2
        const val LOOPER_TYPE_ANDROID_MAIN = 0

        @IntDef(value = [LOOPER_TYPE_BG, LOOPER_TYPE_ANDROID_MAIN, LOOPER_TYPE_REQUEST, LOOPER_TYPE_TEMP])
        annotation class ThreadType

        //后台线程、用户处理数据存储，业务逻辑等
        private const val THREAD_NAME_BG = "BG"
        //网络请求线程，用于分发网络请求，网络请求的处理利用临时线程池
        private const val THREAD_NAME_REQUEST = "REQUEST_THREADS"
        private const val THREAD_NAME_MAIN = "MAIN"
        //临时线程，用于通过handle处理一些临时逻辑、延迟逻辑
        private const val THREAD_NAME_TEMP = "TEMP"
        private const val MAX_RUNNING_THREAD = 3
        private const val EXECUTOR_NAME_TEMP = "TEMP_THREADS"

        @Volatile
        private var m_instance: ThreadManager? = null

        fun getInstance(): ThreadManager {
            if (m_instance == null) {
                synchronized(ThreadManager::class.java) {
                    if (m_instance == null) {
                        m_instance = ThreadManager()
                    }
                }
            }
            return m_instance!!
        }
    }
}