package com.sample.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.github.guqt178.fragment.AppleFragment
import com.sample.test.R

class ResultFragment : AppleFragment() {

    private var mEditText: EditText? = null
    private var mBtnBack: Button? = null


    override fun initView(view: View, savedInstanceState: Bundle?) {
        mEditText = view.findViewById<View>(R.id.edit) as EditText
        mBtnBack = view.findViewById<View>(R.id.btn_back) as Button
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        mBtnBack?.setOnClickListener { v: View? ->
            var result = mEditText?.text.toString()
            if (TextUtils.isEmpty(result)) {
                result = "孔数路"
            }
            val bundle = Bundle()
            bundle.putString("message", result)
            setResult(RESULT_OK, bundle)
            finish()
        }
    }

    override fun layoutId(): Int {
        return R.layout.fragment_result
    }
}