package com.github.guqt178.gui.widget.banner.transform;


import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 侧滑缩小切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:13
 */
public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.9f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position >= -1 || position <= 1) {
            // Modify the default slide transition to shrink the page as well
            final float height = page.getHeight();
            final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            final float vertMargin = height * (1 - scaleFactor) / 2;
            final float horzMargin = page.getWidth() * (1 - scaleFactor) / 2;

            // Center vertically
            page.setPivotY(0.5f * height);

            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }
    }
}
