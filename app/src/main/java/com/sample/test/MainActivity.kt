package com.sample.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.guqt178.utils.dialog.showLoading
import com.github.guqt178.utils.view.doOnClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListener()
    }

    private fun setListener() {
        tv1.doOnClick {
        }

        tv3.doOnClick {
            showLoading("waitig...", false)
        }

        tv2.doOnClick {
        }
        tv4.doOnClick {

        }
    }
}
