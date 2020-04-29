package com.github.guqt178.keyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.inputmethodservice.KeyboardView
import android.support.annotation.IntegerRes
import android.util.AttributeSet
import android.util.TypedValue
import com.github.guqt178.keyboard.NumberKeyboardView

/**
 * Created by Jooyer on 2018/5/9
 * 数字小键盘
 */
class NumberKeyboardView : KeyboardView {
    private var rKeyBackground: Drawable? = null
    private var mPaint: Paint? = null
    private var rLabelTextSize = 0
    private var rKeyTextSize = 0
    private var rKeyTextColor = 0
    private var rShadowRadius = 0f
    private var rShadowColor = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.NumberKeyboardView)
        rKeyBackground = arr.getDrawable(R.styleable.NumberKeyboardView_keyBackground)
        if (null == rKeyBackground) {
            rKeyBackground = context.resources.getDrawable(R.drawable.key_number_bg)
        }
        rLabelTextSize = arr.getDimensionPixelSize(R.styleable.NumberKeyboardView_labelTextSize, 18)
        rKeyTextSize = arr.getDimensionPixelSize(R.styleable.NumberKeyboardView_keyTextSize, 18)
        rKeyTextColor = arr.getColor(R.styleable.NumberKeyboardView_keyTextColor, -0x1000000)
        rShadowColor = arr.getColor(R.styleable.NumberKeyboardView_shadowColor, 0)
        rShadowRadius = arr.getFloat(R.styleable.NumberKeyboardView_shadowRadius, 0f)
        arr.recycle()
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.textSize = rKeyTextSize.toFloat()
        mPaint!!.textAlign = Paint.Align.CENTER
        mPaint!!.alpha = 255
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onRefreshKey(canvas)
    }

    /**
     * onRefreshKey是对父类的private void onBufferDraw()进行的重写.
     * 只是在对key的绘制过程中进行了重新设置.
     */
    private fun onRefreshKey(canvas: Canvas) {
        val kbdPaddingLeft = paddingLeft
        val kbdPaddingTop = paddingTop
        var keyBackground: Drawable? = null
        mPaint!!.color = rKeyTextColor
        val keys = keyboard.keys
        for (key in keys) {
            keyBackground = key.iconPreview
            if (null == keyBackground) {
                keyBackground = rKeyBackground
            }
            val drawableState = key.currentDrawableState
            keyBackground!!.state = drawableState
            val keyLabel = key.label
            val label = if (keyLabel == null) null else adjustCase(keyLabel).toString()
            val bounds = keyBackground.bounds
            if (key.width != bounds.right ||
                key.height != bounds.bottom) {
                keyBackground.setBounds(0, 0, key.width, key.height)
            }
            canvas.translate(key.x + kbdPaddingLeft.toFloat(), key.y + kbdPaddingTop.toFloat())
            keyBackground.draw(canvas)
            if (label != null) {
                mPaint!!.color = rKeyTextColor
                if (key.codes[0] == getKeyCode(R.integer.action_done)) {
                    mPaint!!.textSize = dp2px(rLabelTextSize).toFloat()
                    mPaint!!.typeface = Typeface.DEFAULT
                    mPaint!!.color = Color.WHITE
                } else if (key.codes[0] == getKeyCode(R.integer.line_feed)) {
                    mPaint!!.textSize = dp2px(16).toFloat()
                    mPaint!!.typeface = Typeface.DEFAULT
                } else if (label.length > 1 && key.codes.size < 2) {
                    mPaint!!.textSize = dp2px(rLabelTextSize).toFloat()
                    mPaint!!.typeface = Typeface.DEFAULT
                } else {
                    mPaint!!.textSize = dp2px(rKeyTextSize).toFloat()
                    mPaint!!.typeface = Typeface.DEFAULT
                }

                // Draw a drop shadow for the text
                mPaint!!.setShadowLayer(rShadowRadius, 0f, 0f, rShadowColor)
                // Draw the text
                canvas.drawText(label,
                        key.width / 2.toFloat(),
                        key.height / 2 + (mPaint!!.textSize - mPaint!!.descent()) / 2,
                        mPaint!!)
                // Turn off drop shadow
                mPaint!!.setShadowLayer(0f, 0f, 0f, 0)
            } else if (key.icon != null) {
                val drawableX = (key.width
                        - key.icon.intrinsicWidth) / 2
                val drawableY = (key.height
                        - key.icon.intrinsicHeight) / 2
                canvas.translate(drawableX.toFloat(), drawableY.toFloat())
                key.icon.setBounds(0, 0,
                        key.icon.intrinsicWidth, key.icon.intrinsicHeight)
                key.icon.draw(canvas)
                canvas.translate(-drawableX.toFloat(), -drawableY.toFloat())
            }
            canvas.translate(-key.x - kbdPaddingLeft.toFloat(), -key.y - kbdPaddingTop.toFloat())
        }
    }

    private fun getKeyCode(@IntegerRes redId: Int): Int {
        return context.resources.getInteger(redId)
    }

    private fun dp2px(def: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def.toFloat(), resources.displayMetrics).toInt()
    }

    private fun adjustCase(label: CharSequence): CharSequence? {
        var label: CharSequence? = label
        if (keyboard.isShifted && label != null && label.length < 3 && Character.isLowerCase(label[0])) {
            label = label.toString().toUpperCase()
        }
        return label
    }

    fun setTextSize(textSize: Int) {
        rLabelTextSize = textSize
    }

    companion object {
        private val TAG = NumberKeyboardView::class.java.simpleName
    }
}