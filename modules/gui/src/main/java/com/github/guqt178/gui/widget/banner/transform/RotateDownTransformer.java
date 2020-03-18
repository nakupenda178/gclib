package com.github.guqt178.gui.widget.banner.transform;


import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 向下旋转切换
 *
 * @author xuexiang
 * @since 2019/1/14 下午10:09
 */
public class RotateDownTransformer implements ViewPager.PageTransformer {

	private static final float ROT_MOD = -15f;

	@Override
	public void transformPage(View page, float position) {
		final float width = page.getWidth();
		final float height = page.getHeight();
		final float rotation = ROT_MOD * position * -1.25f;

		page.setPivotX(width * 0.5f);
		page.setPivotY(height);
		page.setRotation(rotation);
	}
}
