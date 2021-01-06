package com.github.guqt178.utils.ext

import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.widget.EditText


//EditText输入手机号自动带空格(3 4 4格式)
fun EditText.formatPhoneInput() {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            if (charSequence.isNullOrEmpty()) return
            val sb = StringBuilder()
            for (i in charSequence.indices) {
                if (i != 3 && i != 8 && charSequence[i] == ' ') {
                    continue
                } else {
                    sb.append(charSequence[i])
                    if ((sb.length == 4 || sb.length == 9) && sb[sb.length - 1] != ' ') {
                        sb.insert(sb.length - 1, ' ')
                    }
                }
            }
            if (sb.toString() != charSequence.toString()) {
                var index = start + 1
                if (sb[start] == ' ') {
                    if (before == 0) {
                        index++
                    } else {
                        index--
                    }
                } else {
                    if (before == 1) {
                        index--
                    }
                }
                setText(sb.toString())
                setSelection(index)
            }
        }

    })
}

/**
 * 限制EditText最大line,xml中的android:maxLines=''没用
 */
fun EditText.limitLine(maxLine: Int) {

    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            val lines = lineCount
            if (lines > maxLine) {
                var s = editable?.toString() ?: ""
                val start = selectionStart
                val end = selectionEnd

                if (start == end && start < s.length && start > 1) {
                    s = s.substring(0, start - 1) + s.substring(start)
                } else {
                    s = s.substring(0, s.length - 1)
                }
                setText(s)
                setSelection(text.length)
            }
        }
    })
}

/**
 * 设置hint 字体大小
 */
fun EditText.setupHintSize(hintText: String, hintSize: Number) {
    val ss = SpannableString(hintText)
    val ass = AbsoluteSizeSpan(hintSize.toInt(), false) //设置字体大小 true表示单位是sp
    ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.hint = SpannedString(ss)
}
