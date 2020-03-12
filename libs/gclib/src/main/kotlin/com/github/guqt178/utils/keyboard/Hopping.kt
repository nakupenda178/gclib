package com.github.guqt178.utils.keyboard

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import java.lang.ref.WeakReference

/**
 * @author  ss on 2018/12/28 10:51.
 * @desc 软键盘弹出时候,移动布局使view不被遮挡,如登录界面的登陆按钮
 */
class Hopping(activity: AppCompatActivity) : LifecycleObserver, ViewTreeObserver.OnGlobalLayoutListener {

    private var scrollToPosition = 0

    private lateinit var rootView: View//想整体移动的布局

    private lateinit var scrollToView: View//不想被遮挡的view
    private var wr: WeakReference<AppCompatActivity>? = null

    init {
        wr = WeakReference(activity)
        wr?.get()?.lifecycle?.addObserver(this)
    }

    /**
     * 移动rootView使scrollToView不被键盘遮挡
     */
    fun scrollRootViewWhenKeyboardOpen(rootView: View, scrollToView: View) {
        this.rootView = rootView
        this.scrollToView = scrollToView
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)

    }


    override fun onGlobalLayout() {
        val localRoot = rootView
        localRoot.let { root ->
            val rect = Rect()
            //获取root在窗体的可视区域
            root.getWindowVisibleDisplayFrame(rect)
            //获取root在窗体的不可视区域高度(被遮挡的高度)
            val rootInvisibleHeight = root.rootView.height - rect.bottom

            //若不可视区域高度大于150，则键盘显示
            if (rootInvisibleHeight > 150) {
                val location = IntArray(2)
                scrollToView.getLocationInWindow(location)
                //计算root滚动高度，使scrollToView在可见区域的底部
                val scrollHeight = (location[1] + scrollToView.height) - rect.bottom

                //注意，scrollHeight是一个相对移动距离，而scrollToPosition是一个绝对移动距离
                scrollToPosition += scrollHeight
            } else {
                //键盘隐藏
                scrollToPosition = 0
            }

            if (scrollToPosition >= 0)
                root.scrollTo(0, scrollToPosition)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clear() {
        rootView.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        wr?.get()?.lifecycle?.removeObserver(this)
    }
}