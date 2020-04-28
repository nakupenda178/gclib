package com.github.guqt178.ezhttp.callback;


import com.github.guqt178.ezhttp.cache.model.CacheResult;
import com.github.guqt178.ezhttp.model.ApiResult;
import com.github.guqt178.ezhttp.utils.Utils;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <p>描述：提供回调代理</p>
 * 主要用于可以自定义ApiResult<br>
 * 作者： zhouyou<br>
 * 日期： 2017/5/16 17:59 <br>
 * 版本： v1.0<br>
 */
public abstract class CallBackProxy<T extends ApiResult<R>, R> implements IType<T> {
    CallBack<R> mCallBack;

    public CallBackProxy(CallBack<R> callBack) {
        mCallBack = callBack;
    }

    public CallBack getCallBack() {
        return mCallBack;
    }

    @Override
    public Type getType() {//CallBack代理方式，获取需要解析的Type
        Type typeArguments = null;
        if (mCallBack != null) {
            Type rawType = mCallBack.getRawType();//如果用户的信息是返回List需单独处理
            if (List.class.isAssignableFrom(Utils.getClass(rawType, 0)) || Map.class.isAssignableFrom(Utils.getClass(rawType, 0))) {
                typeArguments = mCallBack.getType();
            } else if (CacheResult.class.isAssignableFrom(Utils.getClass(rawType, 0))) {
                Type type = mCallBack.getType();
                typeArguments = Utils.getParameterizedType(type, 0);
            } else {
                Type type = mCallBack.getType();
                typeArguments = Utils.getClass(type, 0);
            }
        }
        if (typeArguments == null) {
            typeArguments = ResponseBody.class;
        }
        Type rawType = Utils.findNeedType(getClass());
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return $Gson$Types.newParameterizedTypeWithOwner(null, rawType, typeArguments);
    }
}
