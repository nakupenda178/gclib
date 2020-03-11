package com.github.guqt178.utils.mgr;

import android.util.Log;

/**
 * 日志打印类，对打印日志进行封装，方便根据日志定位问题
 */
public final class FLog {

    /**
     * 日志回调，将要打印的日志回调给开发者，由开发者将日志输出
     */
    public interface IFLogLogCallback {
        void logD(String tag, String log);
        void logV(String tag, String log);
        void logI(String tag, String log);
        void logW(String tag, String log);
        void logE(String tag, String log);
    }

    private static final int START_STACK_INDEX = 4;
    private static final int PRINT_STACK_COUTN = 2;

    private static IFLogLogCallback logCallback = null;

    public static void setFLogLogCallback(IFLogLogCallback callback){
        logCallback = callback;
    }

    public static void d(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);

        if (logCallback != null) {
            logCallback.logD("FLog", sb.toString());
        } else {
            Log.d("FLog", sb.toString());
        }
    }

    public static void v(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if (logCallback != null) {
            logCallback.logV("FLog", sb.toString());
        } else {
            Log.v("FLog", sb.toString());
        }
    }

    public static void i(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if (logCallback != null) {
            logCallback.logI("FLog", sb.toString());
        } else {
            Log.i("FLog", sb.toString());
        }
    }

    public static void w(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if (logCallback != null) {
            logCallback.logW("FLog", sb.toString());
        } else {
            Log.w("FLog", sb.toString());
        }
    }

    public static void e(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);

        if (logCallback != null) {
            logCallback.logE("FLog", sb.toString());
        } else {
            Log.e("FLog", sb.toString());
        }
    }

    private static void appendStack(StringBuilder sb) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks != null && stacks.length > START_STACK_INDEX) {
            int lastIndex = Math.min(stacks.length-1,START_STACK_INDEX+PRINT_STACK_COUTN);
            for (int i=lastIndex; i >= START_STACK_INDEX; i--) {
                if (stacks[i] == null) {
                    continue;
                }

                String fileName = stacks[i].getFileName();
                if (fileName != null) {
                    int dotIndx = fileName.indexOf('.');
                    if (dotIndx > 0) {
                        fileName = fileName.substring(0, dotIndx);
                    }
                }

                sb.append(fileName);
                sb.append('(');
                sb.append(stacks[i].getLineNumber());
                sb.append(")");
                sb.append("->");
            }
            sb.append(stacks[START_STACK_INDEX].getMethodName());
        }
        sb.append('\n');
    }
}
