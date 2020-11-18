package com.sample.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.guqt178.fragment.AppleFragment
import com.sample.test.R

class MainFragment : AppleFragment() {


    private var mToolbar: Toolbar? = null
    private var mTvMessage: TextView? = null


    override fun initView(view: View, savedInstanceState: Bundle?) {
        mToolbar = view.findViewById(R.id.toolbar)
        mTvMessage = view.findViewById(R.id.tv_message)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        val bundle = arguments
        var message = """
            ${bundle!!.getString("hehe")}
            
            """.trimIndent()
        message += """
            ${bundle.getString("meng")}
            
            """.trimIndent()
        message += """
            ${bundle.getString("bang")}
            
            """.trimIndent()
        message += """
            ${bundle.getString("meme")}
            
            """.trimIndent()

        mTvMessage!!.text = message
    }

    override fun layoutId(): Int {
        return R.layout.fragment_argument
    }
}