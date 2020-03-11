package com.github.guqt178.utils.log;

import org.jetbrains.annotations.NotNull;

/**
 * xLog
 * com.elvishew.xlog.XLog
 */
public class ALog {

    public static void wtf(@NotNull String msg, @NotNull Object... format) {
        LoggerWrapper.getLogger().wtf(msg, format);
    }


    public static void warn(@NotNull String msg, @NotNull Object... format) {
        LoggerWrapper.getLogger().warn(msg, format);

    }


    public static void info(@NotNull String msg, @NotNull Object... format) {
        LoggerWrapper.getLogger().info(msg, format);

    }


    public static void error(@NotNull String msg, @NotNull Object... format) {
        LoggerWrapper.getLogger().error(msg, format);

    }


    public static void debug(@NotNull String msg, @NotNull Object... format) {
        LoggerWrapper.getLogger().debug(msg, format);

    }


    public static void tag(@NotNull String tag) {
        LoggerWrapper.getLogger().tag(tag);

    }

    @NotNull

    public static String getTag() {
        return LoggerWrapper.getLogger().getTag();
    }
}
