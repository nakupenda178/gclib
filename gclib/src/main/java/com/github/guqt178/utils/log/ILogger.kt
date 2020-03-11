package com.github.guqt178.utils.log

interface ILogger {

  fun wtf(
    msg: String,
    vararg format: Any
  )

  fun warn(
    msg: String,
    vararg format: Any
  )

  fun info(
    msg: String,
    vararg format: Any
  )

  fun error(
    msg: String,
    vararg format: Any
  )

  fun debug(
    msg: String,
    vararg format: Any
  )

  fun tag(tag: String)
  fun getTag(): String
}