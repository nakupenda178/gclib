package com.moerlong.baselibrary.data.net

import android.support.v4.util.SimpleArrayMap
import com.moerlong.baselibrary.BuildConfig
import com.moerlong.baselibrary.common.BaseApplication
import com.moerlong.baselibrary.common.BaseConstance
import com.moerlong.baselibrary.common.BaseConstance.Companion.CONN_TIME_OUT
import com.moerlong.baselibrary.common.BaseConstance.Companion.READ_TIME_OUT
import com.moerlong.baselibrary.common.BaseConstance.Companion.WRITE_TIME_OUT
import com.moerlong.baselibrary.utils.UserCache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 目前app有3个BaseUrl,所以用这个创建Retrofit
 * @author  ss on 2019/1/8 14:47.
 */
class RetrofitManager private constructor() {

    companion object {

        fun get() = RetrofitManager()

        private val retrofitMap by lazy {
            SimpleArrayMap<String, ICreator>(4)
        }
    }


    private fun newRetrofitFactory(baseUrl: String) = RetrofitFactory(baseUrl)

    /**
     * host为:http://10.1.2.200:8090
     * 默认的host
     */
    fun create() = retrofitMap.get(BaseConstance.BASE_URL)
            ?: newRetrofitFactory(BaseConstance.BASE_URL).also {
                retrofitMap.put(BaseConstance.BASE_URL, it)
            }

    /**
     * host为:http://app.mointech.lan
     * 该host为李俊他们的php后端
     */
    fun createService() = retrofitMap.get(BaseConstance.BASE_URL_SERVER)
            ?: newRetrofitFactory(BaseConstance.BASE_URL_SERVER).also {
                retrofitMap.put(BaseConstance.BASE_URL_SERVER, it)
            }

    /**
     * host为:http://app.mointech.lan
     * 该host为摩运营彭豪后端
     */
    fun createMyyService() = retrofitMap.get(BaseConstance.BASE_URL_MYY)
            ?: newRetrofitFactory(BaseConstance.BASE_URL_MYY).also {
                retrofitMap.put(BaseConstance.BASE_URL_MYY, it)
            }

    /**
     * host为:http://mservice.moerlong.com:8800
     * 该host为公共服务部门的openapi,(java)
     */
    fun createPublicService() = retrofitMap.get(BaseConstance.BASE_URL_PUBLISH)
            ?: newRetrofitFactory(BaseConstance.BASE_URL_PUBLISH).also {
                retrofitMap.put(BaseConstance.BASE_URL_PUBLISH, it)
            }

    /**
     * 初始化retrofit
     */
    private class RetrofitFactory(baseUrl: String) : ICreator {


        private val retrofit: Retrofit

        init {
            val interceptor = Interceptor {
                val request = it
                        .request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("charset", "utf-8")
                        .addHeader("Authorization", "Bearer ${UserCache.getToken(BaseApplication.context)}")
                        //.addHeader("token", UserCache.getToken(BaseApplication.context).orEmpty())
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


        private fun initClient(interceptor: Interceptor) =
                OkHttpClient
                        .Builder()
                        .addInterceptor(interceptor)
                        .retryOnConnectionFailure(true)
                        .connectTimeout(CONN_TIME_OUT, TimeUnit.SECONDS)
                        .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                        .also {
                            if (BuildConfig.IS_DEBUG) {
                                it.addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY })
                            }
                        }
                        .build()


        override fun <T> createApi(api: Class<T>): T {
            return retrofit.create(api)
        }
    }
}