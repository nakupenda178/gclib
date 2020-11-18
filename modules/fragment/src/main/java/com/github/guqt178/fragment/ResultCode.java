package com.github.guqt178.fragment;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({AppleFragment.RESULT_OK, AppleFragment.RESULT_CANCELED})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultCode {
}
