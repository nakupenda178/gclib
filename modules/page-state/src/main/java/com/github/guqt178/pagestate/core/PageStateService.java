package com.github.guqt178.pagestate.core;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.guqt178.pagestate.callback.LayoutState;
import com.github.guqt178.pagestate.callback.SuccessLayoutState;

import java.util.List;

/**
 * 入口
 *
 * @param <T>
 */
public class PageStateService<T> {
    private PageLayout pageLayout;
    private Convertor<T> convertor;

    PageStateService(Convertor<T> convertor,
                     TargetContext targetContext,
                     LayoutState.OnReloadListener onReloadListener,
                     PageState.Builder builder) {
        this.convertor = convertor;
        Context context = targetContext.getContext();
        View oldContent = targetContext.getOldContent();
        ViewGroup.LayoutParams oldLayoutParams = oldContent.getLayoutParams();
        pageLayout = new PageLayout(context, onReloadListener);
        pageLayout.setupSuccessLayout(new SuccessLayoutState(oldContent, context,
                onReloadListener));
        if (targetContext.getParentView() != null) {
            targetContext.getParentView().addView(pageLayout, targetContext.getChildIndex(), oldLayoutParams);
        }
        initLayoutState(builder);
    }

    private void initLayoutState(PageState.Builder builder) {
        List<LayoutState> layoutStates = builder.getLayoutStates();
        Class<? extends LayoutState> defaultLayoutState = builder.getDefaultLayoutState();
        if (layoutStates != null && layoutStates.size() > 0) {
            for (LayoutState layoutState : layoutStates) {
                pageLayout.setupLayoutState(layoutState);
            }
        }
        if (defaultLayoutState != null) {
            pageLayout.showLayoutState(defaultLayoutState);
        }
    }

    public void showSuccess() {
        pageLayout.showLayoutState(SuccessLayoutState.class);
    }

    public void showLayoutState(Class<? extends LayoutState> layoutState) {
        pageLayout.showLayoutState(layoutState);
    }

    public void showWithConvertor(T t) {
        if (convertor == null) {
            throw new IllegalArgumentException("You haven't set the Convertor.");
        }
        pageLayout.showLayoutState(convertor.map(t));
    }

    public PageLayout getPageLayout() {
        return pageLayout;
    }

    public Class<? extends LayoutState> getCurrentLayoutState() {
        return pageLayout.getCurrentLayoutState();
    }

    /**
     * obtain rootView if you want keep the toolbar in Fragment
     *
     * @since 1.2.2
     * @deprecated
     */
    public LinearLayout getTitleLoadLayout(Context context, ViewGroup rootView, View titleView) {
        LinearLayout newRootView = new LinearLayout(context);
        newRootView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        newRootView.setLayoutParams(layoutParams);
        rootView.removeView(titleView);
        newRootView.addView(titleView);
        newRootView.addView(pageLayout, layoutParams);
        return newRootView;
    }

    /**
     * modify the callback dynamically
     *
     * @param layoutState  which callback you want modify(layout, event)
     * @param transport a interface include modify logic
     * @since 1.2.2
     */
    public PageStateService<T> setLayoutState(Class<? extends LayoutState> layoutState, Transport transport) {
        pageLayout.setLayoutState(layoutState, transport);
        return this;
    }
}
