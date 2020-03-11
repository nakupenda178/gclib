package com.github.guqt178.utils.log;

import org.jetbrains.annotations.NotNull;

final class LoggerWrapper implements ILogger {

  private static volatile ILogger mLogger;

  private LoggerWrapper() {
    throw new IllegalStateException("Don`t instantiate ME!!");
  }

  public static ILogger getLogger() {
    if (null == mLogger) {
      synchronized (LoggerWrapper.class) {
        if (null == mLogger) {
          mLogger = new LoggerImpl();
        }
      }
    }
    return mLogger;
  }

  @Override
  public void wtf(@NotNull String msg, @NotNull Object... format) {
    getLogger().wtf(msg, format);
  }

  @Override
  public void warn(@NotNull String msg, @NotNull Object... format) {
    getLogger().warn(msg, format);
  }

  @Override
  public void info(@NotNull String msg, @NotNull Object... format) {
    getLogger().info(msg, format);
  }

  @Override
  public void error(@NotNull String msg, @NotNull Object... format) {
    getLogger().error(msg, format);
  }

  @Override
  public void debug(@NotNull String msg, @NotNull Object... format) {
    getLogger().debug(msg, format);
  }

  @Override
  public void tag(@NotNull String tag) {
    getLogger().tag(tag);
  }

  @NotNull
  @Override
  public String getTag() {
    return getLogger().getTag();
  }
}
