package com.github.guqt178.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.ArrayMap
import android.util.Log
import com.github.guqt178.helper.ActivityResultHelper
import com.github.guqt178.helper.ContainerActivity
import com.github.guqt178.helper.OnResult
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * @author  sugar on 2018/6/27 14:19.
 */


inline fun <reified F : Fragment> Fragment.startContainerActivity(
    bundle: Bundle? = null
) {
    activity?.startContainerActivity(F::class.java.canonicalName, bundle)
}

inline fun <reified F : Fragment> Fragment.startContainerActivityForResult(
    bundle: Bundle? = null,
    requestCode: Int,
    onResult: OnResult? = null
) {
    activity?.startContainerActivityForResult(
        F::class.java.canonicalName,
        bundle,
        requestCode,
        onResult
    )
}

inline fun <reified F : Fragment> Context?.startContainerActivity(
    bundle: Bundle? = null
) {
    startContainerActivity(F::class.java.canonicalName, bundle)
}

/**
 * 在FragmentActivity中调用该方法时onResult会起作用
 */
inline fun <reified F : Fragment> FragmentActivity.startContainerActivityForResult(
    bundle: Bundle? = null,
    onResult: OnResult? = null
) {
    val intent = Intent(this, ContainerActivity::class.java)
    intent.putExtra(ContainerActivity.CLASS_NAME, F::class.java.canonicalName)
    intent.putExtras(bundle ?: Bundle())
    ActivityResultHelper.init(this).startActivityForResult(intent, onResult)

}

/**
 * 单个activity承载fragment,这样不用每次都要写一个activity
 */
fun Context?.startContainerActivity(
    clazzCanonicalName: String,
    bundle: Bundle? = null
) {
    this?.let {
        var intent = Intent(it, ContainerActivity::class.java)
        intent.putExtra(ContainerActivity.CLASS_NAME, clazzCanonicalName)
        intent.putExtras(bundle ?: Bundle())
        (it as? Activity)?.startActivity(intent)
    }
}

/**
 * 单个activity承载fragment,这样不用每次都要写一个activity
 * @tips 在fragment中调用该方法时onResult会起作用
 */
fun Context?.startContainerActivityForResult(
    clazzCanonicalName: String,
    bundle: Bundle? = null,
    requestCode: Int = 9,
    onResult: OnResult? = null//(requstCode,resultCode,data)
) {
    this?.let {
        var intent = Intent(it, ContainerActivity::class.java)
        intent.putExtra(ContainerActivity.CLASS_NAME, clazzCanonicalName)
        intent.putExtras(bundle ?: Bundle())
        ContainerActivity.addOnResultListener(requestCode, onResult)
        (it as? Activity)?.startActivityForResult(intent, requestCode)
    }
}

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

/**
 * <p>
 * 将javaBean对象转换为Map<String, String>形式:
 * 省去需要自己创建Map,一个一个放key和value
 * </p>
 */
inline fun <reified T : Serializable> T.toMap(): Map<String, String> {
    val start = SystemClock.currentThreadTimeMillis()
    val resultMap = ArrayMap<String, String>()
    T::class.members.forEach {
        if (it is KProperty) {
            resultMap[it.name] = (it.call(this) as? String).orEmpty()
        }
    }
    val end = SystemClock.currentThreadTimeMillis()
    Log.e("tag", "function cost time =  ${end - start} ms")
    return resultMap
}