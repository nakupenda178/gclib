package com.github.guqt178.binding.viewadapter.recyclerview

import android.support.annotation.IntDef
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.github.guqt178.binding.command.BindingFunction

/**
 * RecyclerView 的布局管理器类，提供便捷的方法
 */
object LayoutManagers {
    /**
     * 垂直线性布局，从上往下
     */
    @JvmStatic
    fun verticalLinear(): BindingFunction<RecyclerView, RecyclerView.LayoutManager> {
        return BindingFunction { LinearLayoutManager(it.context) }
    }

    /**
     * 线性布局，可控制方向
     */
    @JvmStatic
    fun linear(
        @Orientation orientation: Int,
        reverseLayout: Boolean
    ): BindingFunction<RecyclerView, RecyclerView.LayoutManager> {
        return BindingFunction {
            LinearLayoutManager(
                it.context,
                orientation,
                reverseLayout
            )
        }
    }

    /**
     * 垂直网格布局
     */
    @JvmStatic
    fun verticalGrid(spanCount: Int): BindingFunction<RecyclerView, RecyclerView.LayoutManager> {
        return BindingFunction {
            GridLayoutManager(
                it.context,
                spanCount
            )
        }
    }

    /**
     * 垂直的网格布局
     *
     * @param spanCount 多少列
     * @param lookup    控制每个布局占多少列
     */
    @JvmStatic
    fun verticalGrid(
        spanCount: Int,
        lookup: GridLayoutManager.SpanSizeLookup?
    ): BindingFunction<RecyclerView, RecyclerView.LayoutManager> {
        return BindingFunction {
            val manager = GridLayoutManager(it.context, spanCount)
            manager.spanSizeLookup = lookup
            manager
        }
    }

    /**
     * 网格布局，可控制方向
     */
    @JvmStatic
    fun grid(
        spanCount: Int,
        @Orientation orientation: Int,
        reverseLayout: Boolean
    ): BindingFunction<RecyclerView, RecyclerView.LayoutManager> {
        return BindingFunction {
            GridLayoutManager(
                it.context,
                spanCount,
                orientation,
                reverseLayout
            )
        }
    }

    /**
     * 交错网格布局
     */
    @JvmStatic
    fun staggeredGrid(
        spanCount: Int,
        @Orientation orientation: Int
    ): BindingFunction<RecyclerView, RecyclerView.LayoutManager> {
        return BindingFunction { it: RecyclerView? ->
            StaggeredGridLayoutManager(
                spanCount,
                orientation
            )
        }
    }

    @IntDef(LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Orientation
}