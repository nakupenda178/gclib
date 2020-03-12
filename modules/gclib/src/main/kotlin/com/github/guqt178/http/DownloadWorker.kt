package com.github.guqt178.http

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import com.github.guqt178.http.retrofit.RetrofitManager
import com.github.guqt178.utils.log.Alog
import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 下载文件
 */
class DownloadWorker {

    private var context: Context? = null
    private val BASE_URL_DOWNLOAD = "http://192.168.0.1/"

    private var ROOT = Environment.getExternalStorageDirectory().absolutePath


    @SuppressLint("CheckResult")
    fun download(fileUrl: String, onError: (() -> Unit)? = null, callBack: (File) -> Unit) {
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
                        Alog.debug("file [${it?.absoluteFile?.name}] dont exist")
                }, {
                    it.printStackTrace()
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
            val saveFileRoot = File(createDownloadFile())
            val isPic = body.contentType()?.type()?.contains("image") ?: true
            val saveFile = File(saveFileRoot, generateFileName(isPic))
            if (!saveFileRoot.exists()) {
                saveFileRoot.mkdirs()
                saveFile.createNewFile()
            }
            /*if (!saveFileRoot.parentFile.exists()) { // 事先判断 ta 的父文件夹是否存在
                val mkdirs: Boolean = saveFileRoot.parentFile.mkdirs()
                Alog.debug(" file = ${saveFileRoot.parentFile.absolutePath.toString()};mkdirs = $mkdirs" )
            }
            if (!saveFile.exists()) {
                val mkdirs: Boolean = saveFileRoot.mkdirs()
                Alog.debug("makir result=$mkdirs")
                if (!mkdirs) { // 如果 建立文件夹失败 将父文件夹删除
                    if (saveFileRoot.parentFile.exists()) {
                        Alog.debug("delete root file =$mkdirs")
                        saveFileRoot.parentFile.delete()
                    }
                }
            }*/
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

    private fun getLifeProvider() = context as? LifecycleProvider<*>

    //下载目录
    //private fun createDownloadFile(): String = "$ROOT${File.separator}${context?.packageName}${File.separator}download"
    private fun createDownloadFile(): String = "$ROOT/${context?.packageName}/download"

    private interface InternalApi {
        @Streaming
        @GET
        fun entry(@Url fileUrl: String): Flowable<ResponseBody>
    }

    companion object {

        //fun fork(life: LifecycleProvider<*>) = SingleTon.INSTANCE.also { it.life = life }

        fun fork(context: Context) = SingleTon.INSTANCE.also { it.context = context }
    }

    private class SingleTon {
        companion object {
            val INSTANCE = DownloadWorker()
        }
    }

}