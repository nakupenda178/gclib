package com.github.guqt178.widgets.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 *    github : https://github.com/getActivity/AndroidProject
 *    desc   : 图片选择列表分割线
 */
public final class GridSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public GridSpaceDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {}

    @SuppressWarnings("all")
    @Override
    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        int position = recyclerView.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanCount();

        // 每一行的最后一个才留出右边间隙
        if ((position + 1) % spanCount == 0) {
            rect.right = mSpace;
        }

        // 只有第一行才留出顶部间隙
        if (position < spanCount) {
            rect.top = mSpace;
        }

        rect.bottom = mSpace;
        rect.left = mSpace;
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {}
}