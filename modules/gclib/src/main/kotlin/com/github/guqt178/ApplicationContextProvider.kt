package com.github.guqt178

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.github.guqt178.utils.GlobalContext


/**
 * 全局Context无侵入式获取
 * 原理是:使用一个ContentProvider，ContentProvider的onCreate()方法调用时，调用getContext()即可获取到Context，再静态变量保存，后续直接获取即可。
 */
class ApplicationContextProvider : ContentProvider() {

    override fun query(uri: Uri, projection: Array<String?>?, selection: String?, selectionArgs: Array<String?>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String?>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String?>?): Int {
        return 0
    }

    override fun onCreate(): Boolean {
        GlobalContext.init(context)
        mContext = context
        return false
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null

        fun getContext() = mContext ?: throw IllegalStateException("Have you declare [ApplicationContextProvider] in your manifest ?")
    }
}