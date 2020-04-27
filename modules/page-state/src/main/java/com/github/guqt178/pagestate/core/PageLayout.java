package com.github.guqt178.pagestate.core;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.github.guqt178.pagestate.ViewInfoUtil;
import com.github.guqt178.pagestate.callback.LayoutState;
import com.github.guqt178.pagestate.callback.SuccessLayoutState;

import java.util.HashMap;
import java.util.Map;

/**
 * 承装状态
 */
public class PageLayout extends FrameLayout {
    private Map<Class<? extends LayoutState>, LayoutState> layoutStates = new HashMap<>();
    private Context context;
    private LayoutState.OnReloadListener onReloadListener;
    private Class<? extends LayoutState> preLayoutState;
    private Class<? extends LayoutState> curLayoutState;
    private static final int CALLBACK_CUSTOM_INDEX = 1;

    public PageLayout(@NonNull Context context) {
        super(context);
    }

    public PageLayout(@NonNull Context context, LayoutState.OnReloadListener onReloadListener) {
        this(context);
        this.context = context;
        this.onReloadListener = onReloadListener;
    }

    public void setupSuccessLayout(LayoutState layoutState) {
        addLayoutState(layoutState);
        View successView = layoutState.getRootView();
        successView.setVisibility(View.GONE);
        addView(successView);
        curLayoutState = SuccessLayoutState.class;
    }

    public void setupLayoutState(LayoutState layoutState) {
        LayoutState cloneLayoutState = layoutState.copy();
        cloneLayoutState.setLayoutState(null, context, onReloadListener);
        addLayoutState(cloneLayoutState);
    }

    public void addLayoutState(LayoutState layoutState) {
        if (!layoutStates.containsKey(layoutState.getClass())) {
            layoutStates.put(layoutState.getClass(), layoutState);
        }
    }

    public void showLayoutState(final Class<? extends LayoutState> layoutState) {
        checkLayoutStateExist(layoutState);
        if (ViewInfoUtil.isMainThread()) {
            showLayoutStateView(layoutState);
        } else {
            postToMainThread(layoutState);
        }
    }

    public Class<? extends LayoutState> getCurrentLayoutState() {
        return curLayoutState;
    }

    private void postToMainThread(final Class<? extends LayoutState> status) {
        post(new Runnable() {
            @Override
            public void run() {
                showLayoutStateView(status);
            }
        });
    }

    private void showLayoutStateView(Class<? extends LayoutState> status) {
        if (preLayoutState != null) {
            if (preLayoutState == status) {
                return;
            }
            layoutStates.get(preLayoutState).onDetach();
        }
        if (getChildCount() > 1) {
            removeViewAt(CALLBACK_CUSTOM_INDEX);
        }

        for (Class key : layoutStates.keySet()) {
            if (key == status) {
                SuccessLayoutState successCallback = (SuccessLayoutState) layoutStates.get(SuccessLayoutState.class);
                if (key == SuccessLayoutState.class) {
                    successCallback.show();
                } else {
                    successCallback.showWithCallback(layoutStates.get(key).getSuccessVisible());
                    View rootView = layoutStates.get(key).getRootView();
                    addView(rootView);
                    layoutStates.get(key).onAttach(context, rootView);
                }
                preLayoutState = status;
            }
        }
        curLayoutState = status;
    }

    public void setLayoutState(Class<? extends LayoutState> layoutState, Transport transport) {
        if (transport == null) {
            return;
        }
        checkLayoutStateExist(layoutState);
        transport.order(context, layoutStates.get(layoutState).obtainRootView());
    }

    private void checkLayoutStateExist(Class<? extends LayoutState> layoutState) {
        if (!layoutStates.containsKey(layoutState)) {
            throw new IllegalArgumentException(String.format("The Callback (%s) is nonexistent.", layoutState
                    .getSimpleName()));
        }
    }
}
