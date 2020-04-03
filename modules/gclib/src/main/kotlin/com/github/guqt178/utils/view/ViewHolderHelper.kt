package com.github.guqt178.utils.view

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IdRes
import android.text.util.Linkify
import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.animation.AlphaAnimation
import android.widget.*
import com.github.guqt178.DefaultAction

/**
 * view包装类
 */
class ViewHolderHelper(private val contentView: View?) {
    private var mViewCache = SparseArray<View>()


    /**
     * 通过@_root_ide_package_.android.support.annotation.IdRes viewId获取控件
     *
     * @param @_root_ide_package_.android.support.annotation.IdRes viewId
     * @return
     */
    inline fun <reified T : View> getView(@IdRes viewId: Int): T? {
        var view = getPool().get(viewId)
        if (view == null) {
            view = getContentView()?.findViewById(viewId) as? T
            getPool().put(viewId, view)
        }
        return view as? T
    }

    fun getContentView() = contentView


    fun getPool() = mViewCache


    /**
     * 设置TextView的值
     *
     * @param @_root_ide_package_.android.support.annotation.IdRes viewId
     * @param text
     * @return
     */
    fun setText(@IdRes viewId: Int, text: String?) = this.apply {
        getView<TextView>(viewId)?.text = text
    }

    fun setSelected(@IdRes viewId: Int, selected: Boolean) = this.apply {
        getView<View>(viewId)?.isSelected = selected
    }

    fun setImageResource(@IdRes viewId: Int, resId: Int) = this.apply {
        getView<ImageView>(viewId)?.setImageResource(resId)
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?) = this.apply {
        getView<ImageView>(viewId)?.setImageBitmap(bitmap)
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?) = this.apply {
        getView<ImageView>(viewId)?.setImageDrawable(drawable)
    }

    fun setBackgroundColor(@IdRes viewId: Int, color: Int) = this.apply {
        getView<View>(viewId)?.setBackgroundColor(color)
    }

    fun setBackgroundRes(@IdRes viewId: Int, backgroundRes: Int) = this.apply {
        getView<View>(viewId)?.setBackgroundResource(backgroundRes)
    }

    fun setTextColor(@IdRes viewId: Int, textColor: Int) = this.apply {
        getView<TextView>(viewId)?.setTextColor(textColor)
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorInt textColor: Int) = this.apply {
        getView<TextView>(viewId)?.setTextColor(textColor)
    }

    fun setAlpha(@IdRes viewId: Int, value: Float) = this.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)?.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)?.startAnimation(alpha)
        }
    }

    fun setVisible(@IdRes viewId: Int, visible: Boolean) = this.apply {
        getView<View>(viewId)?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun linkify(@IdRes viewId: Int) = this.apply {
        val view = getView<TextView>(viewId)
        view?.let {
            Linkify.addLinks(it, Linkify.ALL)
        }
    }

    fun setTypeface(typeface: Typeface?, vararg @IdRes viewIds: Int) = this.apply {
        for (@IdRes viewId in viewIds) {
            getView<TextView>(viewId)?.let {
                it.setTypeface(typeface)
                it.paintFlags = it.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
            }
        }
    }

    fun setProgress(@IdRes viewId: Int, progress: Int) = this.apply {
        getView<ProgressBar>(viewId)?.progress = progress
    }

    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int) = this.apply {
        getView<ProgressBar>(viewId)?.let {
            it.max = max
            it.progress = progress
        }
    }

    fun setMax(@IdRes viewId: Int, max: Int) = this.apply {
        getView<ProgressBar>(viewId)?.max = max
    }

    fun setRating(@IdRes viewId: Int, rating: Float) = this.apply {
        getView<RatingBar>(viewId)?.rating = rating
    }

    fun setRating(@IdRes viewId: Int, rating: Float, max: Int) = this.apply {
        getView<RatingBar>(viewId)?.let {
            it.max = max
            it.rating = rating
        }
    }

    fun setTag(@IdRes viewId: Int, tag: Any?) = this.apply {
        getView<View>(viewId)?.tag = tag
    }

    fun setTag(@IdRes viewId: Int, key: Int, tag: Any?) = this.apply {
        getView<View>(viewId)?.setTag(key, tag)
    }

    fun setChecked(@IdRes viewId: Int, checked: Boolean) = this.apply {
        val view = getView<View>(viewId) as? Checkable
        view?.isChecked = checked
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(@IdRes viewId: Int,
                           listener: DefaultAction?) = this.apply {
        getView<View>(viewId)?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                listener?.invoke()
            }

        })
    }

    fun setOnTouchListener(@IdRes viewId: Int,
                           listener: OnTouchListener?) = this.apply {
        getView<View>(viewId)?.setOnTouchListener(listener)
    }

    fun setOnLongClickListener(@IdRes viewId: Int, listener: OnLongClickListener?) = this.apply {
        getView<View>(viewId)?.setOnLongClickListener(listener)
    }


}