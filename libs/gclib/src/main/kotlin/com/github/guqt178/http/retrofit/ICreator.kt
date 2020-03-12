package com.moerlong.baselibrary.data.net

/**
 * @author  ss on 2019/1/8 14:48.
 */
interface ICreator {
    fun <T> createApi(api: Class<T>): T
}