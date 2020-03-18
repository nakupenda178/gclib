package com.github.guqt178.gui.widget.banner.anim.unselect;


import android.animation.ObjectAnimator;
import android.view.View;

import com.github.guqt178.gui.widget.banner.anim.BaseAnimator;

public class NoAnimExist extends BaseAnimator {

    public NoAnimExist() {
        this.mDuration = 200;
    }

    @Override
    public void setAnimation(View view) {
        this.mAnimatorSet.playTogether(ObjectAnimator.ofFloat(view, "alpha", 1, 1));
    }
}
