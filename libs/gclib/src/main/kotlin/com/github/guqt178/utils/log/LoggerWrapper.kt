package com.github.guqt178.utils.log

internal class LoggerWrapper private constructor() : ILogger {
    override fun wtf(msg: String, vararg format: Any) {
        logger!!.wtf(msg, *format)
    }

    override fun warn(msg: String, vararg format: Any) {
        logger!!.warn(msg, *format)
    }

    override fun info(msg: String, vararg format: Any) {
        logger!!.info(msg, *format)
    }

    override fun error(msg: String, vararg format: Any) {
        logger!!.error(msg, *format)
    }

    override fun debug(msg: String, vararg format: Any) {
        logger!!.debug(msg, *format)
    }

    override fun tag(tag: String) {
        logger!!.tag(tag)
    }

    override fun getTag(): String {
        return logger!!.getTag()
    }

    companion object {
        @Volatile
        private var mLogger: ILogger? = null

        @JvmStatic
        val logger: ILogger?
            get() {
                if (null == mLogger) {
                    synchronized(LoggerWrapper::class.java) {
                        if (null == mLogger) {
                            mLogger = LoggerImpl()
                        }
                    }
                }
                return mLogger
            }
    }

    init {
        throw IllegalStateException("Don`t instantiate ME!!")
    }
}