package com.github.guqt178.http

import android.annotation.SuppressLint
import android.os.Environment
import com.moerlong.baselibrary.data.net.RetrofitManager
import com.moerlong.baselibrary.utils.err
import com.trello.rxlifecycle2.LifecycleProvider
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
 * 下载图片或者视频
 */
class DownloadWorker {

    private lateinit var life: LifecycleProvider<*>

    private class SingtonHolder {
        companion object {
            val INSTANCE = DownloadWorker()
        }
    }

    @SuppressLint("CheckResult")
    fun download(fileUrl: String, onError: (() -> Unit)? = null,callBack: (File) -> Unit) {
        err("fileUrl --$fileUrl")
        val temp = "http://182.150.25.63:8800//dev.maf.melms.utility.file.fileservicecom/api/fileservice/getfile?id=420628934451691520"
        RetrofitManager
                .get()
                .create()
                .createApi(InternalApi::class.java)
                .entry(fileUrl)
                .subscribeOn(Schedulers.io())
                .compose(life.bindToLifecycle())
                .map {
                    err("thread--${Thread.currentThread().name}")
                    writeResponseBodyToDisk(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it?.isFile == true && it?.exists())
                        callBack.invoke(it)
                    else
                        err("file [${it?.absoluteFile?.name}] dont exist")
                }, {
                    it.printStackTrace()
                }, {

                })
    }

    private var DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator + "com.jxcrm.moerlong/download"

    private fun writeResponseBodyToDisk(body: ResponseBody?): File? {
        var tempFile: File? = null
        if (body == null) {
            //ToastUtils.showToast("图片源错误");
            return tempFile
        }
        var fis: InputStream? = null
        val bis: BufferedInputStream? = null
        var fos: OutputStream? = null
        try {
            fis = body.byteStream()
            val saveFileRoot = File(DIR)
            val isPic = body.contentType()?.type()?.contains("image") ?: true
            val saveFile = File(saveFileRoot, generateFileName(isPic))
            if (!saveFileRoot.exists()) {
                saveFileRoot.mkdirs()
            }
            fos = FileOutputStream(saveFile, false)
            val bis = BufferedInputStream(fis)
            val buffer = ByteArray(1024)
            while (true) {
                var len = bis.read(buffer)
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

    private fun generateFileName(isPic: Boolean): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())
        return if (isPic) "IMG_${timeStamp}.jpg" else "video_${timeStamp}.mp4"
    }

    private interface InternalApi {
        @Streaming
        @GET
        fun entry(@Url fileUrl: String): Flowable<ResponseBody>
    }

    companion object {
        fun get(life: LifecycleProvider<*>) = DownloadWorker.SingtonHolder.INSTANCE.also { it.life = life }
    }
}