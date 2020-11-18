package com.sample.test

import android.os.Bundle
import com.github.guqt178.fragment.AppleActivity
import com.sample.fragment.IndexFragment


class NoFragmentActivity : AppleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        startFragment(IndexFragment::class.java)
    }

    override fun fragmentLayoutId(): Int  = R.id.fragment
}
