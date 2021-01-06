package com.github.guqt178.utils.ext

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat


/*保留几位小数*/
fun Number?.format(keep: Int = 2): String {
    this?.let {
        return try {
            String.format("%.${keep}f", this)/*保留keep位小数*/
        } catch (e: Exception) {
            "format exception"/*解析出错*/
        }
    } ?: return "empty data"/*空数据*/
}

/*keepDot-是否保留小数点后2位*/
fun String?.thousandFormat(keepDot: Boolean = false, precision: Int = 2): String {
    this?.let {
        return try {
            return if (keepDot) it.toDouble().thousandFormat(keepDot, precision)
            else it.toInt().thousandFormat()
        } catch (e: Exception) {
            "format exception"/*解析出错*/
            "0.00"
        }
    } ?: return "empty data"/*空数据*/
}

/*保留小数点后2位*/
fun String?.floatFormat(precision: Int = 2): String {
    this?.let {
        return try {
            return it.toDouble().thousandFormat(true, precision)
        } catch (e: Exception) {
            e.printStackTrace()
            //"format exception"/*解析出错*/
            this
        }
    } ?: return "empty data"/*空数据*/
}

fun String?.wan2Yuan():Double{
    if(this.isNullOrEmpty()) return 0.0
    if(!this.isValidNumber()) return 0.0
    return this.toDouble().times(10_000)
}

fun String?.yuan2Wan():Double{
    if(this.isNullOrEmpty()) return 0.0
    if(!this.isValidNumber()) return 0.0
    return this.toDouble().div(10_000)
}
//数字对比
fun String?.greaterThan(other:Number = 100_000_000.0):Boolean{
    return if(this.isNullOrEmpty()) false
    else BigDecimal(this).minus(BigDecimal(other.toString())).toFloat() >0.0f

}


/*千分位表示*/
fun Number?.thousandFormat(keep: Boolean = true, precision: Int = 2): String {
    this?.let {
        val decimal = BigDecimal(it.toString())
        return try {
            when {
                it is Int -> {
                    val format = DecimalFormat("#,##0")
                    format.roundingMode = RoundingMode.HALF_UP
                    format.format(decimal.toInt())
                }
                !keep -> {
                    val format = DecimalFormat("#,##0")
                    format.roundingMode = RoundingMode.HALF_UP
                    format.format(decimal.toDouble())
                }
                else -> {
                    val format = DecimalFormat(if (precision == 2) "#,##0.00" else "#,##0.000")
                    format.roundingMode = RoundingMode.HALF_UP
                    format.format(decimal.toDouble())
                }

            }

        } catch (e: Exception) {
            "format exception"/*解析出错*/
        }
    } ?: return "empty data"/*空数据*/
}


fun String?.isValidNumber(): Boolean {
    if (this.isNullOrEmpty()) return false
    else return try {
        this!!.toDouble()
        return true
    } catch (e: Exception) {
        false
    }
}

fun String?.toDoubleWithDefault(default: Double = 0.0): Double {
    return try {
        this?.toDouble() ?: default
    } catch (e: Exception) {
        default
    }
}