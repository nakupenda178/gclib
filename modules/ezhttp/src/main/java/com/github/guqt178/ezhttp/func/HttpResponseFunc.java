package com.github.guqt178.ezhttp.func;


import android.support.annotation.NonNull;

import com.github.guqt178.ezhttp.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * <p>描述：异常转换处理</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/15 16:55 <br>
 * 版本： v1.0<br>
 */
public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(ApiException.handleException(throwable));
    }
}
