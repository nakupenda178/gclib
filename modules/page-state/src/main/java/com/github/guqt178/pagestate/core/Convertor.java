package com.github.guqt178.pagestate.core;

import com.github.guqt178.pagestate.callback.LayoutState;

public interface Convertor<T> {
   Class<?extends LayoutState> map(T t);
}
