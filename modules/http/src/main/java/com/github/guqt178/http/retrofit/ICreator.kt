package com.github.guqt178.http.retrofit

/**
 * @author  ss on 2019/1/8 14:48.
 */
interface ICreator {
    fun <T> createApi(api: Class<T>): T
}