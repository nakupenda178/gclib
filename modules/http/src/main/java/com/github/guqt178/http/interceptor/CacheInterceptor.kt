package com.github.guqt178.http.interceptor

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 无网络状态下智能读取缓存的拦截器
 */
class CacheInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (isConnected(context)) {
            val response = chain.proceed(request)
            // read from cache for 60 s
            val maxAge = 60
            response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
        } else {
            //读取缓存信息
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            val response = chain.proceed(request)
            //set cache times is 3 days
            val maxStale = 60 * 60 * 24 * 3
            response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
        }
    }

    private fun isConnected(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (manager != null) {
            val info = manager.activeNetworkInfo
            return info != null && info.isAvailable
        }
        return false
    }
}