package com.github.guqt178.http.retrofit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Android 优雅地处理后台返回的骚数据
 * 后台返回结构跟文档不一样，我要将 data 解析成一个对象，而后台返回的是一个空字符串、整形或空数组，肯定解析报错
 * 使用
 * retrofit = Retrofit.Builder()
// 其它配置
.addConverterFactory(MyGsonConverterFactory.create())
.build()
 */
class MyGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return MyGsonResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return MyGsonRequestBodyConverter(gson, adapter)
    }

    companion object {
        @JvmStatic
        fun create(): MyGsonConverterFactory {
            return create(Gson())
        }

        @JvmStatic
        fun create(gson: Gson?): MyGsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return MyGsonConverterFactory(gson)
        }
    }
}
