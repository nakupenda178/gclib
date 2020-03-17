package com.github.guqt178.utils.input

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.NonNull
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

/**
 * 限制输入框的输入
 */
class InputValueFilter(@NonNull private val lifecycleOwner: LifecycleOwner) : TextWatcher, LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private var mEditText: EditText? = null
    private var mPattern: Pattern? = null

    /**
     * 筛选条件
     */
    private var mRegex: String? = null

    /**
     * 默认的筛选条件(正则:只能输入中文)
     */
    private val DEFAULT_REGEX = "[^\u4E00-\u9FA5]"

    /**
     * 使EditText输入限制生效
     * @param editText 目标
     * @param regx 匹配这个正则的都不允许输入,eg,"[^\\d]"表示editText不允许非数字的输入(实现上是让不是数字的都替换为空字符串)
     */
    @JvmOverloads
    private fun attachTo(editText: EditText, regx: String = DEFAULT_REGEX) {
        mRegex = regx
        mEditText = editText
        mPattern = Pattern.compile(regx)
        editText.addTextChangedListener(this)
    }

    /**
     * 只允许输入数字[0-9]
     * @param includeDot 是否包含小数点,默认不包含
     */
    fun digitOnly(editText: EditText,includeDot:Boolean = false) {
        val reg = if(includeDot) "[\\D.]" else "[\\D]"
        attachTo(editText,reg)
    }

    /**
     * 只允许输入中文
     */
    fun chineseOnly(editText: EditText){
        attachTo(editText)

    }

    /**
     * 只允许字母
     * @param upperAllow 是否允许大写,默认允许
     */
    fun letterOnly(editText: EditText,upperAllowed:Boolean = true){
        val reg = if(upperAllowed) "[^A-Za-z]" else "[^a-z]"
        attachTo(editText,reg)
    }

    override fun afterTextChanged(editable: Editable?) {
        val input = editable?.toString()
        val inputStr = mPattern?.matcher(input)?.replaceAll("")
        mEditText?.removeTextChangedListener(this)
        // et.setText方法可能会引起键盘变化,所以用editable.replace来显示内容
        editable?.replace(0, editable.length, inputStr?.trim())
        mEditText?.addTextChangedListener(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun releaseResource() {
        //Log.e("InputValueFilter",">>Lifecycle.Event.ON_DESTROY<<")
        mEditText?.removeTextChangedListener(this)
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    override fun beforeTextChanged(editable: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(editable: CharSequence?, start: Int, before: Int, count: Int) {
    }
}