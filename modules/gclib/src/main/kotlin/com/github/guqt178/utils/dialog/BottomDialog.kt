package com.github.guqt178.utils.dialog

import android.content.Context
import android.graphics.Color
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.github.guqt178.utils.helper.ViewHolderHelper

/**
 * @author  s   ugar on 2020/04/21 10:05
 * @description BottomSheetDialog辅助类
 */
class BottomDialog(@NonNull context: Context,
                   @LayoutRes layoutId: Int,
                   config: (BottomDialog, ViewHolderHelper) -> Unit /*给外部做一些配置*/) : BottomSheetDialog(context) {
    private var helper: ViewHolderHelper? = null
    private val rootView: View by lazy { LayoutInflater.from(context).inflate(layoutId, null) }

    init {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        helper = ViewHolderHelper(rootView)
        config(this, helper!!)
        setContentView(rootView)
        //解决圆角无效
        delegate?.findViewById<View>(android.support.design.R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun showNow() = this.apply {
        super.show()
    }

    /**
     * 点击外部消失
     */
    fun autoDismiss(yes: Boolean = true) = this.apply {
        setCanceledOnTouchOutside(yes)
        setCancelable(yes)
    }

    fun <T : View> findView(@IdRes id: Int) = helper?.getView<View>(id) as? T

    fun setText(@IdRes id: Int, text: String) {
        findView<TextView>(id)?.text = text
    }
}