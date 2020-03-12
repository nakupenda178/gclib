package com.github.guqt178.utils.result

import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity

/**
 * 借助fragment优雅地申请权限和处理 onActivityResult
 * @see RouterFragment
 *
 * @see "https://blog.csdn.net/gdutxiaoxu/article/details/86498647"
 */
class ActivityResultHelper private constructor(activity: FragmentActivity) {
  private val mContext: Context
  private val mRouterFragment: RouterFragment?
  private fun getRouterFragment(activity: FragmentActivity): RouterFragment? {
    var routerFragment: RouterFragment? = findRouterFragment(activity)
    if (routerFragment == null) {
      routerFragment = RouterFragment.newInstance()
      val fragmentManager = activity.supportFragmentManager
      fragmentManager
          .beginTransaction()
          .add(
              routerFragment,
              TAG
          )
          .commitAllowingStateLoss()
      fragmentManager.executePendingTransactions()
    }
    return routerFragment
  }

  private fun findRouterFragment(activity: FragmentActivity): RouterFragment {
    return activity.supportFragmentManager
        .findFragmentByTag(
            TAG
        ) as RouterFragment
  }

  fun startActivityForResult(
    clazz: Class<*>?,
    callback: OnResult?
  ) {
    val intent = Intent(mContext, clazz)
    startActivityForResult(intent, callback)
  }

  fun startActivityForResult(
    intent: Intent?,
    callback: OnResult?
  ) {
    mRouterFragment!!.startActivityForResult(intent, callback)
  }

  companion object {
    private const val TAG = "ActivityLauncher"
    fun init(activity: FragmentActivity): ActivityResultHelper {
      return ActivityResultHelper(
          activity
      )
    }
  }

  init {
    mContext = activity
    mRouterFragment = getRouterFragment(activity)
  }
}