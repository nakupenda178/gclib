package com.sample.test

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.github.guqt178.http.DownloadWorker
import com.github.guqt178.utils.log.Alog
import com.github.guqt178.utils.thread.newThread
import com.github.guqt178.utils.view.onClick
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newThread {
            Alog.debug("current thread name is ${Thread.currentThread()},tag name =${Alog.tag}")
            Alog.tag("MainActivity")
            Thread.sleep(1000)
            Alog.debug("current thread name is ${Thread.currentThread()},tag name =${Alog.tag}")

        }

        tv1.onClick {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android
                    .Manifest.permission.WRITE_EXTERNAL_STORAGE,android
                    .Manifest.permission.READ_EXTERNAL_STORAGE,android
                    .Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            /*//创建文件夹
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory() + "/aa/bb/");
                if (!file.exists()) {
                    Log.d("jim", "path1 create:" + file.mkdirs());
                }*/
            val url = "http://test.storage.edaicrm.com/FrJ3-96zC_l_fahSG9CJd62Cve3C"
            DownloadWorker.fork(this).download(url) {
                Alog.debug("download success file path =${it.absolutePath}")
            }
        }
    }

}
