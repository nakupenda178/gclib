package com.github.guqt178.utils.result

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import java.util.*

/**
 * 无界面fragment
 * 优雅地申请权限和处理 onActivityResult
 * 不需要重写Activity的onActivityResult
 *
 */
class RouterFragment : Fragment() {
    private val mCallbacks = SparseArray<OnResult>()
    private val mCodeGenerator = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startActivityForResult(intent: Intent?, callback: OnResult?) {
        val requestCode = makeRequestCode()
        mCallbacks.put(requestCode, callback)
        startActivityForResult(intent, requestCode)
    }

    private fun makeRequestCode(): Int {
        var requestCode: Int
        var tryCount = 0
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF)
            tryCount++
        } while (mCallbacks.indexOfKey(requestCode) >= 0 && tryCount < 10)
        return requestCode
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = mCallbacks[requestCode]
        mCallbacks.remove(requestCode)
        callback?.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        fun newInstance(): RouterFragment {
            return RouterFragment()
        }
    }
}