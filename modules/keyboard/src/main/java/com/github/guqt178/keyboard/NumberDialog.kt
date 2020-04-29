package com.github.guqt178.keyboard

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.WindowManager
import android.widget.EditText


/**
 * Dialog使用方式:
 * https://blog.csdn.net/fightfightfight/article/details/51169616
 *
 *
 * Created by Jooyer on 2018/6/4
 * 基于 DialogFragment 实现的键盘面板
 */
class NumberDialog : DialogFragment() {
    private var mEditText: EditText? = null
    private var mKeyboardUtil: KeyboardHelper? = null
    private var mOnDoneListener: OnDoneListener? = null
    private var mType: KeyboardType = KeyboardType.NORMAL


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mKeyboardUtil = KeyboardHelper.create(activity!!, mType)
        mKeyboardUtil!!.bindEditText(mEditText!!)
        mKeyboardUtil!!.setOnDoneListener(object : OnDoneListener {
            override fun onDone(text: String) {
                hideSoftKeyboard()
                mOnDoneListener?.onDone(text)
            }

            override fun onHide() {
                hideSoftKeyboard()
            }

        })
        //Log.i("NumberDialog", "30-------------> ")
        return AlertDialog.Builder(activity)
                .setView(mKeyboardUtil!!.getContainerView())
                .create()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.dimAmount = 0f // 不变暗
        params?.windowAnimations = R.style.KeyboardAnim
        window?.attributes = params

        // 这里用透明颜色替换掉系统自带背景
        val color = ContextCompat.getColor(context!!, android.R.color.transparent)
        window?.setBackgroundDrawable(ColorDrawable(color))
    }


    fun setType(type: KeyboardType): NumberDialog {
        mType = type
        return this
    }

    fun bindEditText(editText: EditText): NumberDialog {
        mEditText = editText;
        return this
    }

    fun setOnDoneListener(onDoneListener: OnDoneListener): NumberDialog {
        mOnDoneListener = onDoneListener;
        return this
    }


    fun hideSoftKeyboard() {
        mKeyboardUtil!!.hideSoftKeyboard()
        dismiss()
    }

    companion object {
        fun newInstance(): NumberDialog {
            return NumberDialog()
        }
    }
}
