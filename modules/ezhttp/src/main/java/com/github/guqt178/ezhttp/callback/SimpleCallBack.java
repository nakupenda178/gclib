package com.github.guqt178.ezhttp.callback;

/**
 * <p>描述：简单的回调,默认可以使用该回调，不用关注其他回调方法</p>
 * 使用该回调默认只需要处理onError，onSuccess两个方法既成功失败<br>
 * 作者： zhouyou<br>
 * 日期： 2016/12/29 10:06<br>
 * 版本： v2.0<br>
 */
public abstract class SimpleCallBack<T> extends CallBack<T> {

    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {

    }
}
