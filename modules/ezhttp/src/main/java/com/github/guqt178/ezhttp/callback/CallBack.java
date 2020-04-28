package com.github.guqt178.ezhttp.callback;



import com.github.guqt178.ezhttp.exception.ApiException;
import com.github.guqt178.ezhttp.utils.Utils;

import java.lang.reflect.Type;

/**
 * <p>描述：网络请求回调</p>
 * 作者： zhouyou<br>
 * 日期： 2016/12/28 16:54<br>
 * 版本： v2.0<br>
 */
public abstract class CallBack<T> implements IType<T> {
    public abstract void onStart();

    public abstract void onCompleted();

    public abstract void onError(ApiException e);

    public abstract void onSuccess(T t);

    @Override
    public Type getType() {//获取需要解析的泛型T类型
        return Utils.findNeedClass(getClass());
    }

    public Type getRawType() {//获取需要解析的泛型T raw类型
        return Utils.findRawType(getClass());
    }
}
