package com.moerlong.baselibrary.data.net

import com.moerlong.baselibrary.common.BaseApplication
import com.moerlong.baselibrary.common.BaseConstance
import com.moerlong.baselibrary.utils.UserCache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Dsh  on 2018/4/13.
 */
class RetrofitFactory private constructor():ICreator {

    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val retrofit: Retrofit
    private val interceptor: Interceptor

    init {
        interceptor = Interceptor {
            val request = it
                    .request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("charset", "utf-8")
                    .addHeader("Authorization", "Bearer ${UserCache.getToken(BaseApplication.context)}")
                    .build()
            it.proceed(request)
        }

        retrofit = Retrofit
                .Builder()
                .baseUrl(BaseConstance.BASE_URL)
                .client(initClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient
                .Builder()
                .addInterceptor(initLogInterceptor())
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(BaseConstance.CONN_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(BaseConstance.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(BaseConstance.READ_TIME_OUT, TimeUnit.SECONDS)
                .build()
    }

    private fun initLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }


   override fun <T> createApi(api: Class<T>): T {
        return retrofit.create(api)
    }

}