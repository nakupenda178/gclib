package com.sample.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.guqt178.utils.log.ALog
import com.github.guqt178.utils.mgr.FLog
import com.github.guqt178.utils.onClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_widget_demo)
        val json = "{\"code\":0,\"message\":\"success\",\"data\":{\"isUpdate\":false,\"mode\":0,\"version\":\"\",\"updateUrl\":\"\",\"upgradeInfo\":\"\"}}"
        tv1.onClick { ALog.error(json) }
        tv2.onClick { FLog.v(json) }
        tv3.onClick { ALog.debug("this is message") }
        tv4.onClick {

            try {
                1 / 0
            } catch (e: Exception) {
                ALog.error(e.message.orEmpty())
                FLog.d(e.message.orEmpty())
            }
        }


    }
}
