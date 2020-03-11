package com.github.guqt178.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import java.net.URLConnection
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @author Dsh  on 2018/4/12.
 */


fun View?.onClick(onClickListener: View.OnClickListener) {
    this?.setOnClickListener(onClickListener)
}


/*
 * 传入一个lambda表达式来进行扩展(只需要关心你执行的方法)
 */
fun View?.onClick(method: () -> Unit) {
    this?.setOnClickListener { method() }
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
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
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


fun Context.px2dp(px: Number) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    px.toFloat(),
    this.resources.displayMetrics
)


/**
 * 新建一个线程运行任务
 */
fun execute(t: () -> Unit) {
    val namedThreadFactory = ThreadFactoryBuilder()
        .setNameFormat("my-thread-pool-%d").build()
    val singleThreadPool = ThreadPoolExecutor(
        1, 1,
        0L, TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(1024), namedThreadFactory, ThreadPoolExecutor.AbortPolicy()
    )
    singleThreadPool.execute { t() }
    singleThreadPool.shutdown()
}


// 判断是否符合大陆身份证号码的规范
fun isIdCard(IDCard: String?): Boolean {
    if (IDCard != null) {
        val regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)"
        return Pattern.matches(regex, IDCard)
    }
    return false
}


// 完整的判断中文汉字和符号
@SuppressWarnings("unused")
fun String?.isChinese(): Boolean {
    if (this.isNullOrEmpty())
        return false
    else {
        val ch = this!!.toCharArray()
        for (char in ch) {
            if (isChinese(char)) {
                return true
            }
        }
        return false
    }

}

// 根据Unicode编码完美的判断中文汉字和符号
fun isChinese(c: Char): Boolean {
    val ub = Character.UnicodeBlock.of(c)
    return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
}



/**
 * 避免每次都写subscribeOn(..).observeOn(..)
 * 结合compose操作符使用
 */
fun <T> commonTransformer() = ObservableTransformer<T, T> { upstream ->
    upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun guessMimeType(path: String): MediaType? {
    var path = path
    val fileNameMap = URLConnection.getFileNameMap()
    path = path.replace("#", "")
    var contentType = fileNameMap.getContentTypeFor(path)
    if (contentType == null) {
        contentType = "application/octet-stream"
    }
    return MediaType.parse(contentType)
}

/*打开软键盘*/
inline fun Context.openKeyboard(view: View) {
    with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
        showSoftInput(view, InputMethodManager.RESULT_SHOWN)
        toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

/*延时打开软键盘*/
fun Context.openKeyboardDelay(view: View, delay: Long = 200L) {
    Timer().schedule(object : TimerTask() {
        override fun run() {
            with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
                showSoftInput(view, 0)
            }
        }
    }, delay)
}

/*关闭软键盘*/
fun Context.closeKeyboard(view: View) {
    with(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
        hideSoftInputFromWindow(view.windowToken, 0)
    }
}

