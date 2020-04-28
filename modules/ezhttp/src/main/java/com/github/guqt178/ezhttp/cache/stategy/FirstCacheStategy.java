package com.github.guqt178.ezhttp.cache.stategy;



import com.github.guqt178.ezhttp.cache.RxCache;
import com.github.guqt178.ezhttp.cache.model.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;


/**
 * <p>描述：先显示缓存，缓存不存在，再请求网络</p>
 * <-------此类加载用的是反射 所以类名是灰色的 没有直接引用  不要误删----------------><br>
 * 作者： zhouyou<br>
 * 日期： 2016/12/24 10:35<br>
 * 版本： v2.0<br>
 */
final public class FirstCacheStategy extends BaseStrategy {
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        Observable<CacheResult<T>> cache = loadCache(rxCache, type, key, time, true);
        Observable<CacheResult<T>> remote = loadRemote(rxCache, key, source, false);
        return cache.switchIfEmpty(remote);
    }
}
