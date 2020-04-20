package com.github.guqt178.utils.dialog

import android.content.Context
import android.support.annotation.FloatRange
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.github.guqt178.R
import com.github.guqt178.utils.helper.ViewHolderHelper


/**
 * @author  sugar on 2018/8/24 10:24.
 * AlertDialog setView方法的包装类
 */
class SimpleDialog private constructor(private val builder: Builder) {
    private var dialog: AlertDialog
    private var helper: ViewHolderHelper
    private var widthPercent = 0.5f
    private var autoDismiss = true

    init {
        //下面这句可能导致花屏
        //(builder.context as? Activity)?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val customView = LayoutInflater.from(builder.context).inflate(builder.layoutResId, null)
        val bd = AlertDialog.Builder(builder.context, R.style.IosProgressDialog).setView(customView)


        this.widthPercent = builder.widthPercent
        this.autoDismiss = builder.autoDismiss

        dialog = bd.create()
        helper = ViewHolderHelper(customView)

        dialog.setCancelable(autoDismiss)

        builder.config?.invoke(dialog, helper)
    }


    /**
     * show
     */
    fun show() {
        dialog.show()
        setDialogLayoutParams()
    }

    /**
     * 设置dialog宽高
     */
    private fun setDialogLayoutParams() {
        dialog.window?.apply {
            val lp = attributes //获取对话框当前的参数值
            //p.height = d.height * 2 //高度设置为屏幕
            lp.width = (windowManager.defaultDisplay.width * widthPercent).toInt()
            attributes = lp //设置生效
        }
    }


    companion object {
        fun newBuilder(context: Context) = Builder(context, R.style.IosProgressDialog)
    }

    class Builder(internal var context: Context,
                  @StyleRes themeStyleResId: Int) {
        internal var layoutResId: Int = -1
        internal var config: ((AlertDialog, ViewHolderHelper) -> Unit)? = null
        internal var widthPercent = 0.5f
        internal var autoDismiss = true

        fun contentView(@LayoutRes layoutResId: Int) = this.apply {
            this.layoutResId = layoutResId
        }


        fun config(config: ((AlertDialog, ViewHolderHelper) -> Unit)? = null) = this.apply {
            this.config = config
        }

        /**
         *
         * 设置dialog的宽度为屏幕宽的比例
         * */
        fun setWidthPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float = 0.8f) = this.apply {
            this.widthPercent = percent
        }

        /**
         *
         */
        fun autoDismiss(yes: Boolean = true) = this.apply {
            this.autoDismiss = yes
        }

        fun create() = SimpleDialog(this)

        fun show() = create().show()
    }

}