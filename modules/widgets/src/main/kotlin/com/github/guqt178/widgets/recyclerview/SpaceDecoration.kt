package com.github.guqt178.widgets.recyclerview

import android.graphics.Rect
import android.support.v7.widget.RecyclerView

/**
 * @author  ss on 2018/12/11 15:06.
 */
class SpaceDecoration(
    private val left: Number,
    private val top: Number = left,
    private val right: Number = left,
    private val bottom: Number = top
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect?.also {
            it.left = left.toInt()
            it.top = top.toInt()
            it.right = right.toInt()
            it.bottom = bottom.toInt()
        }
    }
}