package com.github.guqt178.binding.viewadapter.edittext

import android.databinding.BindingAdapter
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.github.guqt178.binding.command.BindingConsumer

/**
 * EditText输入文字改变的监听
 */
@BindingAdapter(value = ["textChanged"], requireAll = false)
fun addTextChangedListener(
    editText: EditText,
    textChanged: BindingConsumer<String>?
) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            textChanged?.call(s.toString())
        }

        override fun afterTextChanged(editable: Editable) {}
    })
}