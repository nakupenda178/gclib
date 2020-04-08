package com.github.guqt178.http.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Android 优雅地处理后台返回的骚数据
 * 后台返回结构跟文档不一样，我要将 data 解析成一个对象，而后台返回的是一个空字符串、整形或空数组，肯定解析报错
val okHttpClient = OkHttpClient.Builder()
// 其它配置
.addInterceptor(HandleErrorInterceptor())
.build()
example:
class HandleErrorInterceptor : ResponseBodyInterceptor() {

override fun intercept(response: Response, body: String): Response {
            var jsonObject: JSONObject? = null
            try {
                   jsonObject = JSONObject(body)
            } catch (e: Exception) {
                   e.printStackTrace()
            }
            if (jsonObject != null) {
                   if (jsonObject.optInt("code", -1) != 200 && jsonObject.has("msg")) {
                        throw ApiException(jsonObject.getString("msg"))
                   }
            }
            return response
           }
}


 */
abstract class ResponseBodyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().toString()
        val response = chain.proceed(request)
        response.body()?.let { responseBody ->
            val contentLength = responseBody.contentLength()
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            var buffer = source.buffer()

            if ("gzip".equals(response.headers()["Content-Encoding"], ignoreCase = true)) {
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
            if (contentLength != 0L) {
                return intercept(response, url, buffer.clone().readString(charset))
            }
        }
        return response
    }

    abstract fun intercept(response: Response, url: String, body: String): Response
}



