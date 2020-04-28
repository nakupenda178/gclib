package com.github.guqt178.ezhttp.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * <p>描述：不加载缓存</p>
 * 1.不适用Okhttp自带的缓存<br>
 * 作者： zhouyou<br>
 * 日期： 2016/12/20 10:35<br>
 * 版本： v2.0<br>
 */
public class NoCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().header("Cache-Control", "no-cache").build();
        Response originalResponse = chain.proceed(request);
        originalResponse = originalResponse.newBuilder().header("Cache-Control", "no-cache").build();
        return originalResponse;
    }
}



