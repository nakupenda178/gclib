package com.github.guqt178.keyboard

import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

/**
 * 键盘容器
 */
class KeyboardContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    var keyboardView: NumberKeyboardView? = null
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.key_board_container, this, true)
        keyboardView = view.findViewById<View>(R.id.number_keyboard_view) as NumberKeyboardView
        keyboardView?.isEnabled = true
        keyboardView?.isPreviewEnabled = false
    }

    fun setOnKeyboardActionListener(listener: OnKeyboardActionListener?) {
        keyboardView?.setOnKeyboardActionListener(listener)
    }

    fun setKeyboardType(type: KeyboardType) {
        val xmlRes = when (type) {
            KeyboardType.FLOAT -> {
                R.xml.keyboard_number2
            }
            com.github.guqt178.keyboard.KeyboardType.DOT -> {
                R.xml.keyboard_dot_number
            }
            else -> {
                R.xml.keyboard_number
            }
        }
        val keyboard = Keyboard(context, xmlRes)
        keyboardView?.setTextSize(if (type == com.github.guqt178.keyboard.KeyboardType.DOT) 18 else 24)
        keyboardView?.keyboard = keyboard
    }

}