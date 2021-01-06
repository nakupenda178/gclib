package com.github.guqt178.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.provider.MediaStore
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ThumbnailUtil {


    /**
     * 获取视频缩略图
     */
    @JvmStatic
    fun getThumbnailFromFile(path: String,
                             desireWidth: Int = 1045, //345 155
                             desireHeight: Int = 465): Bitmap {

        //创建一个视频缩略图
        val src = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)
        //指定视频缩略图的大小
        val dst = ThumbnailUtils.extractThumbnail(src, desireWidth, desireHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT)

        return dst
    }

    fun saveBitmap(context: Context, bitmap: Bitmap): File? {
        val file = File(generateFileName(context.externalCacheDir)) //将要保存图片的路径
        try {
            val bos = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return file
    }

    private fun generateFileName(parent: File?): String {
        val path = File(parent, "thumbnail")
        if (!path.exists()) {
            path.mkdirs()
        }

        val dst = File("${path.absolutePath}/thumbnail_${System.currentTimeMillis()}.jpg")
        if(!dst.exists()){
            dst.createNewFile()
        }

        return dst.absolutePath
    }
}