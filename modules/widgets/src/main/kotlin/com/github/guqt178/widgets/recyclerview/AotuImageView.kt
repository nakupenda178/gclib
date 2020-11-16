package com.github.guqt178.widgets.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.github.guqt178.widgets.R

/**
 * 底边可向内凹的imageview,凹的程度可以通过设置app:aiv_bottom_margin或者setBottomMargin控制
 * 主要是在图片上层画一个遮罩层
 */
class AotuImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mWidth: Float = 0f
    private var mHeight: Float = 0f
    private var mPath: Path = Path()
    private var mBottomMargin: Float = -1f
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor("#ffffff")
        context.obtainStyledAttributes(attrs, R.styleable.ArchImageView).apply {
            mBottomMargin = getDimension(R.styleable.ArchImageView_aiv_bottom_margin, dip(25))
            recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        createOverlayPath()
        canvas?.clipPath(mPath) //这种方式边缘有锯齿,无法解决
        super.onDraw(canvas)
        canvas?.drawPath(mPath, mPaint)
    }

    private fun createOverlayPath() {
        mPath.reset()
        mPath.moveTo(0f, mHeight)
        mPath.quadTo(mWidth / 2, mHeight - mBottomMargin, mWidth, mHeight)
        mPath.close()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()

        Log.e("ArchImageView", "height=$mHeight,mwidth=$mWidth")
    }

    fun setBottomMargin(margin: Number) {
        this.mBottomMargin = dip(margin)
        invalidate()
    }

    private fun dip(px: Number): Float {
        return context.resources.displayMetrics.density.times(px.toFloat())
    }
}