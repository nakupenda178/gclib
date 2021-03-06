package com.github.guqt178.utils.ext

import java.util.regex.Pattern


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
 * 判断手机号是否中国大陆
 *
 * */
fun isMainlandPhone(target: String?) = if (target.isNullOrEmpty())
    false
else
    Pattern.compile("^1[3|4|5|6|7|8|9][0-9]{9}\$").matcher(target).matches()


/**
 * 8-16位数字+字母组合的密码格式才对
 * */
fun passwordIsValid(target: String?) =
        Pattern.compile("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,16}\$").matcher(target).matches()
