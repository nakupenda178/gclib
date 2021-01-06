package com.github.guqt178.utils.ext

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


// <editor-fold defaultstate="collapsed" desc="长按ImageView保存图片">
/**
 * 开启缓存保存图片，适用于所有view。但是这种方式保存的图片有个致命的缺
 *点，所见即所得，比如ImageView的缩放类型为ScaleType.CENTER_CROP，
 * 此时保存的也是缩放过后的图片，这种效果可能有时无法满足我们的实际需求。但也不是一无是处，在截屏介绍中，这种方法非常强大
 */
fun View.snapshot(filePath: String?) {
    isDrawingCacheEnabled = true //开启绘制缓存

    val imageBitmap: Bitmap = drawingCache
    var outStream: FileOutputStream? = null
    val file = File(filePath ?: Environment.getDownloadCacheDirectory().absolutePath)
    if (file.isDirectory()) { //如果是目录不允许保存
        return
    }
    try {
        outStream = FileOutputStream(file)
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            outStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        imageBitmap.recycle()
        isDrawingCacheEnabled = false //关闭绘制缓存
    }
}

/**
 * 上面的方式无法保存一张完整的图片，所以可能无法满足实际工作需求，这时就需
 * 要用到下面这种方法，这种方法可以获取整个图片，但是只适用于ImageView。其他
 * view只能使用getBackground来获得Drawable对象，不满足实际需求，而且
 * Background也不一定是BitmapDrawable
 */
fun ImageView.snapshot(path: String?) {
    val drawable: Drawable = drawable ?: return
    var outStream: FileOutputStream? = null
    val file: File = File(path ?: Environment.getDownloadCacheDirectory().absolutePath)
    if (file.isDirectory) { // 如果是目录不允许保存
        return
    }

    try {
        outStream = FileOutputStream(file)
        val bitmap = (drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.flush()
        outStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        outStream?.close()
    }
}

fun ImageView.saveJpg(): Boolean {
    try {
        val drawable = drawable ?: return false
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val dataUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val fileUri: Uri? = context.contentResolver.insert(dataUri, values)

        // 如果保存不成功，insert没有任何错误信息，此时调用update会有错误信息提示
        //getContext().getContentResolver().update(dataUri, values, "", null);
        if (fileUri == null) {
            Log.e("snapshot", "fileUri == null")
            return false
        }
        val outStream: OutputStream? = context.contentResolver.openOutputStream(fileUri)
        val bitmap = (drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream?.flush()
        outStream?.close()

        // 刷新相册
        /*String[] files = new String[1];
            String[] mimeTypes = new String[1];
            files[0] = filePath;
            mimeTypes[0] = "image/jpeg";
            MediaScannerConnection.scanFile(view.getContext(), files, mimeTypes, null);
            */
        context.sendBroadcast(Intent("com.android.camera.NEW_PICTURE", fileUri))
        Log.e("snapshot", "保存图片到相册完毕...路径${fileUri}")
        return true
    } catch (ex: IOException) {
        Log.e("snapshot", ex.message)
    }
    return false
}
// </editor-fold>
