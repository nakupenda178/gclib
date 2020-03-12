package com.github.guqt178.utils.result

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.guqt178.R

/**
 * @author  sugar on 2018/8/23 15:58.
 */
class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.id = R.id.container
        })
        initView()
    }

    private fun initView() {
        val extras = intent?.extras
        val classname = intent?.getStringExtra(CLASS_NAME)
        //ee("classname = $classname")

        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(classname)
        if (null == fragment) {
            try {
                val fragmentClass = Class.forName(classname)
                fragment = (fragmentClass.getConstructor().newInstance() as Fragment).apply {
                    arguments = extras ?: Bundle()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "instantiate fragment($classname) failed!")
            }
        }

        if (null == fragment) {
            Log.e(TAG, "instantiate fragment($classname) failed!")
            return
        }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment, classname)
            //.addToBackStack(classname)
            .hide(fragment)/*触发onHideChange*/
            .show(fragment)/*触发onHideChange*/
            .commitNowAllowingStateLoss()
    }

    companion object {
        private val resultMap: SparseArray<OnResult> = SparseArray(5)
        private const val TAG = "ContainerActivity"

        const val CLASS_NAME = "classname"

        fun addOnResultListener(requestCode: Int, listener: OnResult?) {
            listener?.let {
                if (resultMap.indexOfValue(it) < 0) {
                    resultMap.put(requestCode, it)
                }
            }
        }

        fun removeOnResultListener(listener: OnResult) {
            val indexOfValue = resultMap.indexOfValue(listener)
            if (indexOfValue > 0) {
                resultMap.removeAt(indexOfValue)
            }
        }

        fun clearOnResultListeners() {
            resultMap.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resultMap.get(requestCode)?.onActivityResult(requestCode, resultCode, data)
        resultMap.remove(requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearOnResultListeners()
    }
}

