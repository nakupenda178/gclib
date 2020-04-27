package com.github.guqt178.http

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import com.github.guqt178.http.error.OnErrorCallback
import com.github.guqt178.http.error.OnSuccessCallback
import com.github.guqt178.http.retrofit.RetrofitManager
import com.google.gson.JsonObject
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 获取json请求
 */
class JsonWorker {

    private var context: Context? = null

    private val BASE_URL_DOWNLOAD = "http://127.0.0.1/"

    /**
     * @param url url example:http://www.storage.example.com/oss1239419427120742400.MOV
     */
    @SuppressLint("CheckResult")
    fun get(url: String,
                onError: OnErrorCallback? = null,
                callBack: ((JSONObject) -> Unit)? = null) {
        RetrofitManager
                .createClient(BASE_URL_DOWNLOAD)
                .createApi(InternalApi::class.java)
                .entry(url)
                .subscribeOn(Schedulers.io())
                .let {
                    if (getLifeProvider() != null) {
                        it.compose(getLifeProvider()?.bindToLifecycle())
                    } else {
                        it
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val jsonResq = it.contentType()?.toString()?.contains("application/json") ?: false

                    if (jsonResq) {
                        callBack?.invoke(JSONObject(it.string()))
                    } else {
                        onError?.invoke(RuntimeException("The response type is not application/json "))
                    }
                }, {
                    it.printStackTrace()
                    onError?.invoke(it)
                }, {

                })
    }

    //lifecycle
    private fun getLifeProvider() = context as? LifecycleProvider<*>


    private interface InternalApi {
        @Streaming
        @GET
        fun entry(@Url url: String): Flowable<ResponseBody>
    }

    companion object {

        @JvmStatic
        fun fork(context: Context) = SingleTon.INSTANCE.also { it.context = context }
    }

    private class SingleTon {
        companion object {
            val INSTANCE = JsonWorker()
        }
    }

}