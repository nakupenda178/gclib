package com.github.guqt178.utils.intent

import android.app.Activity
import android.content.Intent
import android.net.Uri


/**
 * 调起百度地图URI
 */
fun Activity?.openBaiduApi(uriStr: String = "baidumap://map/newsassistant?src=andr.baidu.openAPIdemo") {
    this?.let {
        it.startActivity(Intent().apply {
            data = Uri.parse(uriStr)
        })
    }
}
