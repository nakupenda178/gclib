package com.github.guqt178.widgets.state;

import android.content.Context;
import android.util.AttributeSet;


public class StateButton extends StateTextView {

    public StateButton(Context context) {
        this(context, null);
    }

    public StateButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.buttonStyle);
    }

    public StateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
