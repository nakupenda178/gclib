package com.github.guqt178.keyboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener
import android.os.Build
import android.support.annotation.IntegerRes
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout

/**
 * 参考:
 * https://juejin.im/post/5ae0ff7ff265da0b9671e835
 * https://www.jianshu.com/p/b1973de976e4
 * Created by Jooyer on 2018/5/9
 * 自定义键盘工具类
 */
class KeyboardHelper private constructor(context: Activity, type: KeyboardType = KeyboardType.NORMAL) {

    private val mRect = Rect()

    private val mKeyboardView: KeyboardView? = null

    private var mEditText: EditText? = null
    private var mRootView: ViewGroup? = null

    private var keyboardContainer: KeyboardContainer? = null
    private val mKeyboardContainerParams: FrameLayout.LayoutParams


    private val mKeyboardActionListener: OnKeyboardActionListener = object : OnKeyboardActionListener {
        override fun onPress(primaryCode: Int) {
            // 按下 key 时执行
        }

        override fun onRelease(primaryCode: Int) {
            // 释放 key 时执行
        }

        // 点击  key 时执行
        override fun onKey(primaryCode: Int, keyCodes: IntArray) {
            val editable = mEditText!!.text
            val start = mEditText!!.selectionStart
            if (primaryCode == Keyboard.KEYCODE_CANCEL
                || primaryCode == getKeyCode(mEditText!!.context, R.integer.hide_keyboard)) {
                hideSoftKeyboard()
                mOnDoneListener?.onHide()
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) { // 回退
                if (editable != null && editable.length > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start)
                    }
                }
            } else if (primaryCode == getKeyCode(mEditText!!.context, R.integer.action_done)) { // 确定
                hideSoftKeyboard()
                mOnDoneListener?.onDone(mEditText!!.text.toString())
            } else if (primaryCode == getKeyCode(mEditText!!.context, R.integer.line_feed)) { // 换行
                editable!!.insert(start, "\n")
            } else { // 输入键盘值
                editable!!.insert(start, Character.toString(primaryCode.toChar()))
            }
        }

        override fun onText(text: CharSequence) {}
        override fun swipeLeft() {}
        override fun swipeRight() {}
        override fun swipeDown() {}
        override fun swipeUp() {}
    }

    private val mOnLayoutChangeListener: View.OnLayoutChangeListener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int,
                                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
            var hasMoved = 0
            val rootViewTag = mRootView?.getTag(R.id.root_view)
            if (rootViewTag != null) {
                hasMoved = rootViewTag as Int
            }
            if (keyboardContainer?.visibility == View.GONE) {
                mRootView?.removeOnLayoutChangeListener(this)
                if (hasMoved > 0) {
                    mRootView?.getChildAt(0)?.scrollBy(0, -1 * hasMoved)
                    mRootView?.setTag(R.id.root_view, 0)
                }
            } else {
                mRect.setEmpty()
                mRootView?.getWindowVisibleDisplayFrame(mRect)
                val etLocation = IntArray(2)
                mEditText!!.getLocationOnScreen(etLocation)
                var keyboardTop = (etLocation[1] + mEditText!!.height
                        + mEditText!!.paddingTop + mEditText!!.paddingBottom + 1) //1px is a divider
                val anchorTag = mEditText!!.getTag(R.id.anchor_view)
                var mShowAnchorView: View? = null
                if (anchorTag != null && anchorTag is View) {
                    mShowAnchorView = anchorTag
                }
                if (mShowAnchorView != null) {
                    val saLocation = IntArray(2)
                    mShowAnchorView.getLocationOnScreen(saLocation)
                    keyboardTop = saLocation[1] + mShowAnchorView.height + mShowAnchorView.paddingTop + mShowAnchorView //1px is a divider
                            .paddingBottom + 1
                }
                val moveHeight = keyboardTop + (keyboardContainer?.height ?: 0) - mRect.bottom
                //height > 0  , rootView 需要继续上滑
                if (moveHeight > 0) {
                    mRootView?.getChildAt(0)?.scrollBy(0, moveHeight)
                    mRootView?.setTag(R.id.root_view, hasMoved + moveHeight)
                } else {
                    val moveBackHeight = Math.min(hasMoved, Math.abs(moveHeight))
                    if (moveBackHeight > 0) {
                        mRootView?.getChildAt(0)?.scrollBy(0, -1 * moveBackHeight)
                        mRootView?.setTag(R.id.root_view, hasMoved - moveBackHeight)
                    }
                }
            }
        }
    }

    private val mOnFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) { //失去焦点隐藏
            hideSoftKeyboard()
        }
    }

    init {
        mRootView = context.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
        keyboardContainer = KeyboardContainer(context)
        keyboardContainer?.setOnKeyboardActionListener(mKeyboardActionListener)
        mKeyboardContainerParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        mKeyboardContainerParams.gravity = Gravity.BOTTOM
        keyboardContainer?.setKeyboardType(type)
    }

    private fun dp2px(context: Context, def: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def.toFloat(), context.resources.displayMetrics).toInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bindEditText(editText: EditText) {
        mEditText = editText
        mEditText?.tag = 0
        mEditText?.setOnTouchListener { v, event ->
            if (0 == v.tag as Int) {
                mOnDoneListener?.onTouchEditText(mEditText!!)
                showSoftKeyboard()
            }
            if (mEditText!!.text.isNotEmpty()
                && mEditText!!.selectionStart != mEditText!!.text.length) {
                mEditText!!.setSelection(mEditText!!.text.length)
            }
            false
        }
        keyboardContainer?.setOnKeyboardActionListener(mKeyboardActionListener)
        mEditText?.onFocusChangeListener = mOnFocusChangeListener
        hideSystemSoftKeyboard()
    }


    private fun showSoftKeyboard() {
        mEditText?.tag = 1
        mRootView?.addOnLayoutChangeListener(mOnLayoutChangeListener)
        keyboardContainer?.setPadding(
                dp2px(mEditText!!.context, 0),
                dp2px(mEditText!!.context, -1),
                dp2px(mEditText!!.context, 0),
                dp2px(mEditText!!.context, 0))
        if (mRootView?.indexOfChild(keyboardContainer) == -1) { // 这个思路不错哦
            if (null != keyboardContainer?.parent) {
                (keyboardContainer?.parent as ViewGroup).removeView(keyboardContainer)
            }
            mRootView?.addView(keyboardContainer, mRootView?.childCount
                    ?: 0, mKeyboardContainerParams)
        } else {
            keyboardContainer?.visibility = View.VISIBLE
        }
        keyboardContainer?.animation = AnimationUtils.loadAnimation(mEditText!!.context, R.anim.keyboard_anim_bottom_in)
    }

    fun hideSoftKeyboard() {
        if (null != mEditText && -1 != mRootView?.indexOfChild(keyboardContainer)) {
            mEditText!!.tag = 0
            keyboardContainer?.startAnimation(AnimationUtils.loadAnimation(mEditText!!.context, R.anim.keyboard_anim_bottom_out))
            keyboardContainer?.postDelayed({ mRootView?.removeView(keyboardContainer) }, 400)
        }
    }

    private fun hideSystemSoftKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEditText!!.showSoftInputOnFocus = false
        } else {
            mEditText!!.inputType = InputType.TYPE_NULL
        }
    }

    private fun getKeyCode(context: Context, @IntegerRes redId: Int): Int {
        return context.resources.getInteger(redId)
    }

    private var mOnDoneListener: OnDoneListener? = null

    fun setOnDoneListener(onDoneListener: OnDoneListener?) {
        mOnDoneListener = onDoneListener
    }

    fun getContainerView() = keyboardContainer

    companion object {

        private val TAG = KeyboardHelper::class.java.simpleName

        fun create(context: Activity,
                   type: KeyboardType = KeyboardType.NORMAL) = KeyboardHelper(context, type)

        fun bindEditTexts(context: Activity,
                          type: KeyboardType = KeyboardType.NORMAL,
                          vararg editText: EditText) {
            editText.forEach {
                create(context).bindEditText(it)
            }
        }

    }

}