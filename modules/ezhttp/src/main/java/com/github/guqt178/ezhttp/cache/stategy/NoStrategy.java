package com.github.guqt178.ezhttp.cache.stategy;


import com.github.guqt178.ezhttp.cache.RxCache;
import com.github.guqt178.ezhttp.cache.model.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * <p>描述：网络加载，不缓存</p>
 * 作者： zhouyou<br>
 * 日期： 2017/7/29 11:28 <br>
 * 版本： v1.0<br>
 */
public class NoStrategy implements IStrategy {
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String cacheKey, long cacheTime, Observable<T> source, Type type) {
        return source.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(@NonNull T t) throws Exception {
                return new CacheResult<T>(false, t);
            }
        });
    }
}
