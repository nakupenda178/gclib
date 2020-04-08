package com.github.guqt178.http.retrofit

import android.support.v4.util.SimpleArrayMap

/**
 * 多个BaseUrl,可以用这个创建Retrofit
 * @author  gc on 2019/1/8 14:47.
 */
object RetrofitManager {

    private  val retrofitMap by lazy {
        SimpleArrayMap<String, ICreator>(3)
    }


    fun createClient(baseUrl: String) = retrofitMap.get(baseUrl) ?: newRetrofitFactory(baseUrl).also {
        retrofitMap.put(baseUrl, it)
    }


    private fun newRetrofitFactory(baseUrl: String) = RetrofitFactory(baseUrl)
}