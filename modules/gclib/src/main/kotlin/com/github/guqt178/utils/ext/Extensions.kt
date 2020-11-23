package com.github.guqt178.utils.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.support.annotation.IntRange
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.ArrayMap
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.github.guqt178.DefaultConsumer
import com.github.guqt178.utils.keyboard.MoneyValueFilter
import com.github.guqt178.utils.result.ActivityResultHelper
import com.github.guqt178.utils.result.ContainerActivity
import com.github.guqt178.utils.result.OnResult
import com.github.guqt178.utils.thread.doAsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * @author  sugar on 2018/6/27 14:19.
 */
// <editor-fold defaultstate="collapsed" desc="与ActivityResult相关">

inline fun <reified F : Fragment> Fragment.startContainerActivity(
    bundle: Bundle? = null
) {
    activity?.startContainerActivity(F::class.java.canonicalName.orEmpty(), bundle)
}

inline fun <reified F : Fragment> Fragment.startContainerActivityForResult(
    bundle: Bundle? = null,
    requestCode: Int,
    onResult: OnResult? = null
) {
    activity?.startContainerActivityForResult(
            F::class.java.canonicalName.orEmpty(),
            bundle,
            requestCode,
            onResult
    )
}

inline fun <reified F : Fragment> Context?.startContainerActivity(
    bundle: Bundle? = null
) {
    startContainerActivity(F::class.java.canonicalName.orEmpty(), bundle)
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
    ActivityResultHelper.init(this)
            .startActivityForResult(intent, onResult)

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
    onResult: OnResult? = null //(requstCode,resultCode,data)
) {
    this?.let {
        var intent = Intent(it, ContainerActivity::class.java)
        intent.putExtra(ContainerActivity.CLASS_NAME, clazzCanonicalName)
        intent.putExtras(bundle ?: Bundle())
        ContainerActivity.addOnResultListener(requestCode, onResult)
        (it as? Activity)?.startActivityForResult(intent, requestCode)
    }
}

// </editor-fold>

/**
 * <p>
 * 将javaBean对象转换为Map<String, String>形式:
 * 省去需要自己创建Map,一个一个放key和value
 * </p>
 */
inline fun <reified T : Serializable> T.toMap(crossinline onResult: DefaultConsumer<Map<String, Any>>) {

    doAsyncTask<Map<String, Any>>({
        val start = SystemClock.currentThreadTimeMillis()
        val resultMap = ArrayMap<String, Any>()
        T::class.members.forEach {
            if (it is KProperty) {
                resultMap[it.name] = it.call(this)
            }
        }
        val end = SystemClock.currentThreadTimeMillis()
        //Alog.info("to map cost [${end - start}]ms")
        resultMap
    }) {
        onResult.invoke(it)
    }
}


/**
 * 监听加载过程
 */
fun ImageView.loadWithListener(
    context: Context,
    url: Any?,
    listener: ((Drawable?) -> Unit)? = null
) {
    val value = RequestOptions()
    url?.let {
        Glide.with(context)
                .load(it)
                .apply(value)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        listener?.invoke(resource)
                        return false
                    }

                })
                .into(this)
                .clearOnDetach()
    }
}

//从assert文件夹中配置文件，然后转化为json对象
fun Context.readAssetsFile(fileName: String): String {
    val sb = StringBuilder()
    try {
        val br = BufferedReader(InputStreamReader(assets.open(fileName)))
        var line: String?
        while (br.readLine().also { line = it } != null) {
            sb.append(line)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return sb.toString()
}

//

/**
 *
 * 限制EditText的输入项只能是数字
 * @param maxLength 最大输入位数长度,例如,3,表示最大输入3位数,即999.99
 * @param precision 小数点后多少位(0表示只能输入整数)
 */
val BIT_COUNT = arrayOf(0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000, 100000000000)

fun EditText.applyFilter(@IntRange(from = 0, to = 11) maxLength: Int, precision: Int = 2) {
    val tempLength = if (maxLength < 0) 0 else maxLength
    MoneyValueFilter().also {
        it.setMaxValue(BIT_COUNT[tempLength])
        it.setPrecision(precision)
        val customFilters = arrayOf(it)
        this.filters = customFilters
    }
}

//快速排序
// 使用 [2,5,1] -> [1,2,5]
//listOf(2,5,1).quickSort() // [1,2,5]
fun <T : Comparable<T>> List<T>.quickSort(): List<T> =
        if (size < 2) {
            this
        } else {
            val pivot = first()
            val (smaller, greater) = drop(1).partition { it <= pivot }
            smaller.quickSort() + pivot + greater.quickSort()
        }




