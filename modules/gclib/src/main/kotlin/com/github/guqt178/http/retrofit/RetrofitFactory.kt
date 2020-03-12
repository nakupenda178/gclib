package com.github.guqt178.http.retrofit

import com.moerlong.baselibrary.data.net.ICreator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Dsh  on 2018/4/13.
 */
class RetrofitFactory(baseUrl: String) : ICreator {

    private val retrofit: Retrofit

    init {
        val interceptor = Interceptor {
            val request = it
                    .request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("charset", "utf-8")
                    //.addHeader("Authorization", "Bearer ${UserCache.getToken(BaseApplication.context)}")
                    .build()
            it.proceed(request)
        }

        retrofit = Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .client(initClient(interceptor))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun initClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient
                .Builder()
                .addInterceptor(getLogInterceptor())
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(CONN_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build()
    }

    private fun getLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        return interceptor
    }


    override fun <T> createApi(api: Class<T>): T {
        return retrofit.create(api)
    }

    companion object {

        private var isDebug = true
        const val WRITE_TIME_OUT: Long = 60
        const val CONN_TIME_OUT: Long = WRITE_TIME_OUT
        const val READ_TIME_OUT: Long = WRITE_TIME_OUT
    }
}