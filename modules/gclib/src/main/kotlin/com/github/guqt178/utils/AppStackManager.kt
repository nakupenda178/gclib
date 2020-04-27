package com.github.guqt178.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.support.v4.app.Fragment
import java.util.*
import kotlin.system.exitProcess

/**
 * @author gc  on 2018/5/8.
 * activity回退栈管理
 */
class AppStackManager private constructor() {


    private val activityStack: Stack<Activity> = Stack()
    private val fragmentStack: Stack<Fragment> = Stack()

    companion object {
        @JvmStatic
        private val INSTANCE: AppStackManager by lazy { AppStackManager() }

        @JvmStatic
        fun getInstance() = INSTANCE
    }

    /**
     * 入栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /**
     * 出栈
     */
    fun finishActivity(activity: Activity) {
        activity.finish()
        activityStack.remove(activity)
    }

    /**
     * 获取栈顶activity
     */
    fun currentActivity(): Activity {
        return activityStack.lastElement()
    }

    /**
     * 清理栈
     */

    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

    /**
     * 退出app
     */
    @SuppressLint("MissingPermission")
    fun exitApp(context: Context) {
        finishAllActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        exitProcess(0)
    }

    /**
     * 结束掉activity以上的所有activity
     */
    fun popToActivity(cls: Class<*>) {
        while (!activityStack.isEmpty() && activityStack.peek().javaClass != cls) {
            activityStack.pop()?.finish()
        }
    }


    /**
     * 添加Fragment到堆栈
     */
    fun addFragment(fragment: Fragment?) {
        fragmentStack.add(fragment)
    }

    /**
     * 移除指定的Fragment
     */
    fun removeFragment(fragment: Fragment?) {
        if (fragment != null) {
            fragmentStack.remove(fragment)
        }
    }


    /**
     * 是否有Fragment
     */
    fun hasFragment(): Boolean {
        return !fragmentStack.isEmpty()
    }

    /**
     * 获取当前Fragment（堆栈中最后一个压入的）
     */
    fun currentFragment(): Fragment? = fragmentStack.lastElement()

}