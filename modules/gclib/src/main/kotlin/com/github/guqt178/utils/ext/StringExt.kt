package com.github.guqt178.utils.ext

import android.support.annotation.ColorInt
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan

fun String?.ifEmpty(option: String = "") = if (this.isNullOrEmpty()) option else this


/**
 * 156****8978
 * */
@JvmOverloads
fun String?.toHiddenPhoneFormat(ifEmpty: String = "") = this?.let {

    if (!isMainlandPhone(this))
        ifEmpty
    else
        this.substring(0, 3) + "****" + this.substring(7, this.length)
} ?: ifEmpty

/**
 * 数组转list
 */
fun toList(array: Array<String>?): List<String> {
    val result = arrayListOf<String>()
    array?.forEach {
        result.add(it)
    }
    return result
}


//将list转换为带有 ， 的字符串
fun listToString(list: List<String>?): String {
    val sb = StringBuilder()
    if (list != null && list.isNotEmpty()) {
        for (i in list.indices) {
            if (i < list.size - 1) {
                sb.append(list[i] + ",")
            } else {
                sb.append(list[i])
            }
        }
    }
    return sb.toString()
}


/**
 * 去除url指定参数
 * @param url
 * @param name
 * @return
 */
fun removeParam(url: String, vararg name: String): String? {
    var result = url
    for (s in name) {
        // 使用replaceAll正则替换,replace不支持正则
        result = result.replace("&?" + s + "=[^&]*".toRegex(), "")
    }
    return result
}

/**
 * 判断字符串是否是给定pattern格式的日期
 * 将yyyy-MM-dd格式的日期转化为yyyy-MM格式
 */
fun toShortDateString(target: String?): String {
    val longPattern = "^\\d{4}-\\d{1,2}-\\d{1,2}".toRegex() //yyyy-MM-dd
    val shortPattern = "^\\d{4}-\\d{1,2}".toRegex() //yyyy-MM
    return when {
        target?.matches(longPattern) == true -> {
            target.lastIndexOf("-").let {
                if (it != -1)
                    target.substring(0, it)
                else
                    ""
            }
        }

        target?.matches(shortPattern) == true -> target

        else -> target.orEmpty()
    }
}


/**
 * 更改文字中部分字体颜色
 * @param color 变成什么色
 * @param startIndex 开始位置
 * @param count   变色多少个 默认字符串长度
 */
fun CharSequence.changeColor(@ColorInt color: Int, startIndex: Int, count: Int = this.length, flag: Int = SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE) =
        SpannableStringBuilder(this).apply {
            val colorSpan = ForegroundColorSpan(color)
            setSpan(colorSpan, startIndex, startIndex + count, flag)
        }

/**
 * 更改文字中多部分字体颜色
 * @param config 多个部分
 */
fun CharSequence.changeColors(flag: Int = SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE, vararg config: Triple<Int, Int, Int>) =
        SpannableStringBuilder(this).apply {
            config.forEach {
                val color = it.first
                val startIndex = it.second
                val count = it.third
                val colorSpan = ForegroundColorSpan(color)
                setSpan(colorSpan, startIndex, startIndex + count, flag)
            }

        }

/**
 * 更改文字中部分字体大小
 * @param size 变成多大
 * @param startIndex 开始位置
 * @param endIndex   结束位置
 */
fun String.changeSize(size: Int, startIndex: Int, endIndex: Int, flag: Int = SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE) =
        SpannableStringBuilder(this).apply {
            val sizeSpan = AbsoluteSizeSpan(size, true)
            setSpan(sizeSpan, startIndex, endIndex, flag)
        }


