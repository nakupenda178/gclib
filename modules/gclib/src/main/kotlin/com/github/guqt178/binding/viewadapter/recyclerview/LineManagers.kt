package com.github.guqt178.binding.viewadapter.recyclerview

import android.support.v7.widget.RecyclerView
import com.github.guqt178.binding.command.BindingFunction
import com.github.guqt178.binding.viewadapter.recyclerview.DividerLine

/**
 * RecyclerView 分割线
 */
object LineManagers {
    @JvmStatic
    fun both(): BindingFunction<RecyclerView, RecyclerView.ItemDecoration> {
        return BindingFunction {
            DividerLine(
                it.context,
                DividerLine.LINE_BOTH
            )
        }
    }

    @JvmStatic
    fun horizontal(): BindingFunction<RecyclerView, RecyclerView.ItemDecoration> {
        return BindingFunction {
            DividerLine(
                it.context,
                DividerLine.LINE_HORIZONTAL
            )
        }
    }

    @JvmStatic
    fun vertical(): BindingFunction<RecyclerView, RecyclerView.ItemDecoration> {
        return BindingFunction {
            DividerLine(
                it.context,
                DividerLine.LINE_VERTICAL
            )
        }
    }
}