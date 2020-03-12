package com.github.guqt178.utils

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import java.io.Serializable

/**
 * @author  sugar on 2018/8/7 18:19.
 */
object FragmentFactory {

    const val INTENT_PARAM_KEY1 = "intent_param_key1"


    inline fun <reified T : Fragment> create(args: Any? = null): T {
        return try {
            val targetClass = T::class.java.getConstructor().newInstance() as T
            targetClass.arguments = Bundle().apply {
                when (args) {
                    is Int -> putInt(INTENT_PARAM_KEY1, args)
                    is Char -> putChar(INTENT_PARAM_KEY1, args)
                    is Byte -> putByte(INTENT_PARAM_KEY1, args)
                    is Float -> putFloat(INTENT_PARAM_KEY1, args)
                    is String -> putString(INTENT_PARAM_KEY1, args)
                    is Bundle -> putBundle(INTENT_PARAM_KEY1, args)
                    is Boolean -> putBoolean(INTENT_PARAM_KEY1, args)
                    is Parcelable -> putParcelable(INTENT_PARAM_KEY1, args)
                    is Serializable -> putSerializable(INTENT_PARAM_KEY1, args)
                }
            }
            targetClass
        } catch (e: Exception) {
            throw InstantiationException("--FragmentFactory--reflect Constructor method  Faied...")
        }
    }

    fun <T> getSerializable(ft: Fragment) = ft.arguments?.getSerializable(INTENT_PARAM_KEY1) as? T

}