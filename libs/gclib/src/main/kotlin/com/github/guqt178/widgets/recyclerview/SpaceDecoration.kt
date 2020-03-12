package com.moerlong.provider.widgets

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author  ss on 2018/12/11 15:06.
 */
class SpaceDecoration(private val space: Number) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        //super.getItemOffsets(outRect, itemPosition, parent)
        outRect?.also {
            it.left   = space.toInt()
            it.top    = space.toInt()
            //it.right  = space.toInt()
            //it.bottom = space.toInt()
        }
    }
}