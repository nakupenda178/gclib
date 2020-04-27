package com.github.guqt178.pagestate.core;


import android.support.annotation.NonNull;

import com.github.guqt178.pagestate.ViewInfoUtil;
import com.github.guqt178.pagestate.callback.LayoutState;

import java.util.ArrayList;
import java.util.List;

public class PageState {
    private static volatile PageState pageState;
    private Builder builder;

    public static PageState getDefault() {
        if (pageState == null) {
            synchronized (PageState.class) {
                if (pageState == null) {
                    pageState = new PageState();
                }
            }
        }
        return pageState;
    }

    private PageState() {
        this.builder = new Builder();
    }

    private void setBuilder(@NonNull Builder builder) {
        this.builder = builder;
    }

    private PageState(Builder builder) {
        this.builder = builder;
    }

    public PageStateService register(@NonNull Object target) {
        return register(target, null, null);
    }

    public PageStateService register(Object target, LayoutState.OnReloadListener onReloadListener) {
        return register(target, onReloadListener, null);
    }

    public <T> PageStateService register(Object target, LayoutState.OnReloadListener onReloadListener, Convertor<T>
            convertor) {
        TargetContext targetContext = ViewInfoUtil.getTargetContext(target);
        return new PageStateService<>(convertor, targetContext, onReloadListener, builder);
    }

    public static Builder beginBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<LayoutState> layoutStates = new ArrayList<>();
        private Class<? extends LayoutState> defaultLayoutState;

        public Builder addLayoutState(@NonNull LayoutState layoutState) {
            layoutStates.add(layoutState);
            return this;
        }

        public Builder setDefaultLayoutState(@NonNull Class<? extends LayoutState> defaultLayoutState) {
            this.defaultLayoutState = defaultLayoutState;
            return this;
        }

        List<LayoutState> getLayoutStates() {
            return layoutStates;
        }

        Class<? extends LayoutState> getDefaultLayoutState() {
            return defaultLayoutState;
        }

        public void commit() {
            getDefault().setBuilder(this);
        }

        public PageState build() {
            return new PageState(this);
        }

    }
}
