package com.github.guqt178.ezhttp.cache.stategy;



import com.github.guqt178.ezhttp.cache.RxCache;
import com.github.guqt178.ezhttp.cache.model.CacheResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;

/**
 * <p>描述：只请求网络</p>
 *<-------此类加载用的是反射 所以类名是灰色的 没有直接引用  不要误删----------------><br>
 * 作者： zhouyou<br>
 * 日期： 2016/12/24 10:35<br>
 * 版本： v2.0<br>
 */
public final class OnlyRemoteStrategy extends BaseStrategy{
    @Override
    public <T> Observable<CacheResult<T>> execute(RxCache rxCache, String key, long time, Observable<T> source, Type type) {
        return loadRemote(rxCache,key, source,false);
    }
}
