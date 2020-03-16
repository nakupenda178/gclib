package com.github.guqt178.http

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import com.github.guqt178.http.retrofit.RetrofitManager
import com.github.guqt178.utils.log.Alog
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.*
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*


typealias OnErrorCallback = (Throwable) -> Unit
typealias OnSuccessCallback = (File) -> Unit
/**
 * 下载文件
 */
class DownloadWorker {

    private var context: Context? = null

    private val BASE_URL_DOWNLOAD = "http://127.0.0.1/"


    //Android Pie（API 29）谷歌禁止在非自己应用的文件夹下创建文件或者是文件
    private var mDownloadDir = Environment.getExternalStorageDirectory().absolutePath


    /**
     * @param fileUrl 下载的完整url example:http://www.storage.example.com/oss1239419427120742400.MOV
     */
    @SuppressLint("CheckResult")
    fun download(fileUrl: String, onError: OnErrorCallback? = null, callBack: OnSuccessCallback) {
        RetrofitManager
                .createClient(BASE_URL_DOWNLOAD)
                .createApi(InternalApi::class.java)
                .entry(fileUrl)
                .subscribeOn(Schedulers.io())
                .let {
                    if (getLifeProvider() != null) {
                        it.compose(getLifeProvider()?.bindToLifecycle())
                    } else {
                        it
                    }
                }
                .map {
                    writeResponseBodyToDisk(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it?.isFile == true && it.exists())
                        callBack.invoke(it)
                    else
                        onError?.invoke(RuntimeException("File [${it?.absoluteFile?.name}] don`t exist"))
                }, {
                    it.printStackTrace()
                    onError?.invoke(it)
                }, {
                    Alog.debug("onComplete")
                })
    }


    private fun writeResponseBodyToDisk(body: ResponseBody?): File? {
        val tempFile: File? = null
        if (body == null) {
            return tempFile
        }
        var fis: InputStream? = null
        val bis: BufferedInputStream? = null
        var fos: OutputStream? = null
        try {
            fis = body.byteStream()
            val isPicType = body.contentType()?.type()?.contains("image") ?: true

            //create file
            val saveFileRoot = File(createDownloadFileDir())
            val saveFile = File(saveFileRoot, generateFileName(isPicType))
            if (!saveFileRoot.exists()) {
                if (saveFileRoot.mkdirs())
                    saveFile.createNewFile()
            }

            //write file
            fos = FileOutputStream(saveFile, false)
            val bis = BufferedInputStream(fis)
            val buffer = ByteArray(1024)
            while (true) {
                val len = bis.read(buffer)
                if (len == -1) {
                    break
                }
                fos.write(buffer, 0, len);
            }
            fos.flush()
            return saveFile
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bis?.close()
            fis?.close()
            fos?.close()
        }
        return null
    }

    //生成一个文件名
    private fun generateFileName(isPic: Boolean): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
        return if (isPic) "IMG_${timeStamp}.jpg" else "video_${timeStamp}.mp4"
    }

    //lifecycle
    private fun getLifeProvider() = context as? LifecycleProvider<*>

    //下载目录
    private fun createDownloadFileDir(): String = "${context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}"
            .also { mDownloadDir = it }

    private interface InternalApi {
        @Streaming
        @GET
        fun entry(@Url fileUrl: String): Flowable<ResponseBody>
    }

    companion object {

        @JvmStatic
        fun fork(context: Context) = SingleTon.INSTANCE.also { it.context = context }
    }

    private class SingleTon {
        companion object {
            val INSTANCE = DownloadWorker()
        }
    }

}