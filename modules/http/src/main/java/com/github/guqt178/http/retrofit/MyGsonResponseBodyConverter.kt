package com.github.guqt178.http.retrofit

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * Android 优雅地处理后台返回的骚数据
 * 后台返回结构跟文档不一样，我要将 data 解析成一个对象，而后台返回的是一个空字符串、整形或空数组，肯定解析报错
 * 使用
 * retrofit = Retrofit.Builder()
   // 其它配置
        .addConverterFactory(MyGsonConverterFactory.create())
        .build()
 */
class MyGsonResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {

        // 在这里通过 value 拿到 json 字符串进行解析
        // 判断状态码是失败的情况，就抛出异常

        val jsonReader = gson.newJsonReader(value.charStream())
        value.use {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            return result
        }
    }
}
