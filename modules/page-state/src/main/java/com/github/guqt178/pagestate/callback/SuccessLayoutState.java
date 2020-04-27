package com.github.guqt178.pagestate.callback;


import android.content.Context;
import android.view.View;

public class SuccessLayoutState extends LayoutState {
    public SuccessLayoutState(View view, Context context, OnReloadListener onReloadListener) {
        super(view, context, onReloadListener);
    }

    @Override
    protected int onCreateView() {
        return 0;
    }

    /**
     * @deprecated Use {@link #showWithCallback(boolean successVisible)} instead.
     */
    public void hide() {
        obtainRootView().setVisibility(View.INVISIBLE);
    }

    public void show() {
        obtainRootView().setVisibility(View.VISIBLE);
    }

    public void showWithCallback(boolean successVisible) {
        obtainRootView().setVisibility(successVisible ? View.VISIBLE : View.INVISIBLE);
    }

}
