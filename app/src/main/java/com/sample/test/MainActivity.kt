package com.sample.test

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ToastUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor


class MainActivity : AppCompatActivity() {
    private val TAG = "aty"
    private fun createDownloadFileDir(): String {
        return "${externalCacheDir}/download"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*tv1.onClick {
            requestPermisson()
        }*/
        ClickUtils.applyGlobalDebouncing(tv1,2000){
            ToastUtils.showShort(System.currentTimeMillis().toString())
        }
        ClickUtils.applyGlobalDebouncing(tv4,2000){
            showLoading("压缩中...",true)
        }
        ClickUtils.applyGlobalDebouncing(tv3,2000){
            /*SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("Won't be able to recover this file!")
                    .setConfirmText("Yes,delete it!")
                    .setConfirmClickListener{
                        it.dismissWithAnimation()
                    }
                    .show()*/
        }

        tv2.onClick {
           ToastUtils.showCustomShort(TextView(this).also{
               it.backgroundColor = Color.parseColor("#00ff00")
               it.text = "text"
               it.width = 300
               it.height = 300
           })
        }
    }

    fun showLoading(msg: String?, cancleable: Boolean = false) {
        /*if (!DialogMaker.isShowing() && isFinishing.not()) {
            DialogMaker.showProgressDialog(this, msg, cancleable)
        }*/
    }

    private fun doDownload() {
        val url = "http://test.storage.edaicrm.com/oss1239419427120742400.MOV"
       /* DownloadWorker.fork(this).download(url) {
            Alog.debug("download success file path =${it.absolutePath}")
        }*/
    }

    private fun requestPermisson() {
        RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({
                    if (it) doDownload()
                    //else Alog.wtf("获取权限失败")
                }, {
                    it.printStackTrace()
                })
    }

}
