package com.github.guqt178.utils;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import org.jetbrains.annotations.NotNull;

/**
 * @usage <p>
 * Animation animation = new ViewSizeChangeAnimation(imageView, isHide ? 100 : 1000, isHide ? 100 : 1000);
 * animation.setDuration(500);
 * imageView.startAnimation(animation);
 * </p>
 */
public class ViewSizeChangeAnimation extends Animation implements Animation.AnimationListener {
    private int initialHeight;
    private int targetHeight;

    private int initialWidth;
    private int targetWidth;
    private int duration = 300;
    private View view;


    private boolean animIsStart = false;

    private static final int INVALID = -1024;

    ViewSizeChangeAnimation(View view, int targetHeight, int targetWidth) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        setAnimationListener(this);
    }

    public static ViewSizeChangeAnimation ofHeight(View view, int targetHeight) {
        return new ViewSizeChangeAnimation(view, targetHeight, INVALID);
    }

    public static ViewSizeChangeAnimation ofWidth(View view, int targetWidth) {
        return new ViewSizeChangeAnimation(view, INVALID, targetWidth);
    }

    public static ViewSizeChangeAnimation of(View view, int targetWidth, int targetHeight) {
        return new ViewSizeChangeAnimation(view, targetHeight, targetWidth);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = initialHeight + (int) ((targetHeight - initialHeight) * interpolatedTime);
        view.getLayoutParams().width = initialWidth + (int) ((targetWidth - initialWidth) * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.initialHeight = height;
        this.initialWidth = width;

        if (targetHeight == INVALID)
            targetHeight = initialHeight;
        if (targetWidth == INVALID)
            targetWidth = initialWidth;

        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    // <editor-fold defaultstate="collapsed" desc="开启">


    public void start() {
        start(duration);
    }

    public void start(int duration) {
        if (animIsStart) return;


        if (view != null) {
            view.clearAnimation();
            this.duration = duration;
            this.setDuration(duration);
            view.startAnimation(this);
        }
    }

    @Override
    public void startNow() {
        super.startNow();
        start();
    }

    @Override
    public void onAnimationStart(Animation animation) {
        animIsStart = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animIsStart = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    // </editor-fold>
}