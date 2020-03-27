package com.sample.test

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.guqt178.utils.dialog.hideLoading
import com.github.guqt178.utils.dialog.showLoading
import com.github.guqt178.utils.keyboard.hideKeyboard
import com.github.guqt178.utils.keyboard.observeKeyboardEvent
import com.github.guqt178.utils.keyboard.showKeyboardForce
import com.github.guqt178.utils.log.Alog
import com.github.guqt178.utils.thread.SimpleOnTask
import com.github.guqt178.utils.thread.exeAsyncAction
import com.github.guqt178.utils.thread.postDelay
import com.github.guqt178.utils.view.doOnClick
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private fun createDownloadFileDir(): String {
        return "${externalCacheDir}/download"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeKeyboardEvent{
            tv1.text = "key board is Visiable = $it"
        }

        tv1.doOnClick {

            showLoading("请稍后...")
            exeAsyncAction({
                Alog.wtf("${Thread.currentThread().name}-doAsyncResult 3s return resylt>>")
                Thread.sleep(3000)
                "result"
            }, object : SimpleOnTask<Activity, String>() {
                override fun onStart(t: Activity) {
                    postDelay(600){
                        showLoading("上传中...")
                    }
                    Alog.wtf("${Thread.currentThread().name}- onStart ...")

                }

                override fun onResult(r: String) {
                    Alog.wtf("${Thread.currentThread().name}- onResult ...")
                    showLoading("上传成功...")

                    postDelay {
                        hideLoading()
                    }
                }

                override fun onError(th: Throwable) {
                    Alog.wtf("${Thread.currentThread().name}- onError ...")
                    hideLoading()
                }

            })
        }

        tv2.doOnClick {

        }

        button.doOnClick {
            showKeyboardForce()
        }

        button2.doOnClick {
            hideKeyboard()
        }
    }
}
