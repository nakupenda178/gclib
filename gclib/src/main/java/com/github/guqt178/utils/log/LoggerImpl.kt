package com.github.guqt178.utils.log

import android.os.Environment
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import java.io.File

internal class LoggerImpl(
    config: LogConfiguration? = null,
    printers: Array<Printer>? = null
) : ILogger {

    private var isDebug = true
    private var mConfig: LogConfiguration? = null

    private var mPrinters: Array<Printer> = Array(2) { index ->
        when (index) {
            1 -> defaultFilePrinter()
            else -> defaultPrinter()
        }
    }

    init {
        mConfig = config ?: defaultConfig()
        if (!printers.isNullOrEmpty()) {
            mPrinters = printers
        }
        XLog.init(                                                 // Initialize XLog
            mConfig, // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
            *mPrinters
        )
    }

    override fun wtf(
        msg: String,
        vararg format: Any
    ) {
        XLog.w(msg, format)
    }

    override fun warn(
        msg: String,
        vararg format: Any
    ) {
        XLog.w(msg, format)
    }

    override fun info(
        msg: String,
        vararg format: Any
    ) {
        XLog.i(msg, format)
    }

    override fun error(
        msg: String,
        vararg format: Any
    ) {
        XLog.e(msg, format)
    }

    override fun debug(
        msg: String,
        vararg format: Any
    ) {
        XLog.d(msg, format)
    }

    companion object {
        private var M_TAG = "ALOG"
    }

    override fun tag(tag: String) {
        M_TAG = tag
    }

    override fun getTag(): String = M_TAG

    private fun defaultPrinter() = AndroidPrinter()

    private fun defaultFilePrinter() =
        FilePrinter.Builder(// Printer that print the log to the file system
            File(Environment.getExternalStorageDirectory(), "ALggerExample").path
        )            // Specify the path to save log file
            .fileNameGenerator(
                DateFileNameGenerator()
            )        // Default: ChangelessFileNameGenerator("log")
            // .backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
            // .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy()
            .flattener(ClassicFlattener())                     // Default: DefaultFlattener
            .build()

    private fun defaultConfig() = LogConfiguration.Builder()
        .logLevel(
            if (isDebug)
                LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
            else
                LogLevel.NONE
        )
        .tag(M_TAG)                   // Specify TAG, default: "X-LOG"
        // .t()                                                // Enable thread info, disabled by default
        // .st(2)                                              // Enable stack trace info with depth 2, disabled by default
        //.b()
        // Enable border, disabled by default
        // .jsonFormatter(new MyJsonFormatter())               // Default: DefaultJsonFormatter
        // .xmlFormatter(new MyXmlFormatter())                 // Default: DefaultXmlFormatter
        // .throwableFormatter(new MyThrowableFormatter())     // Default: DefaultThrowableFormatter
        // .threadFormatter(new MyThreadFormatter())           // Default: DefaultThreadFormatter
        // .stackTraceFormatter(new MyStackTraceFormatter())   // Default: DefaultStackTraceFormatter
        // .borderFormatter(new MyBoardFormatter())            // Default: DefaultBorderFormatter
        // .addObjectFormatter(AnyClass.class,                 // Add formatter for specific class of object
        //     new AnyClassObjectFormatter())                  // Use Object.toString() by default
        .addInterceptor(
            BlacklistTagsFilterInterceptor(    // Add blacklist tags filter
                "blacklist1", "blacklist2", "blacklist3"
            )
        )
        // .addInterceptor(new WhitelistTagsFilterInterceptor( // Add whitelist tags filter
        //     "whitelist1", "whitelist2", "whitelist3"))
        // .addInterceptor(new MyInterceptor())                // Add a log interceptor
        .build()
}