package com.github.guqt178.ezhttp.request;


import android.support.annotation.NonNull;

import com.github.guqt178.ezhttp.cache.model.CacheResult;
import com.github.guqt178.ezhttp.callback.CallBack;
import com.github.guqt178.ezhttp.callback.CallBackProxy;
import com.github.guqt178.ezhttp.func.ApiResultFunc;
import com.github.guqt178.ezhttp.func.CacheResultFunc;
import com.github.guqt178.ezhttp.func.HandleFuc;
import com.github.guqt178.ezhttp.func.RetryExceptionFunc;
import com.github.guqt178.ezhttp.model.ApiResult;
import com.github.guqt178.ezhttp.subsciber.CallBackSubsciber;
import com.github.guqt178.ezhttp.transformer.HandleErrTransformer;
import com.github.guqt178.ezhttp.utils.RxUtil;
import com.github.guqt178.ezhttp.utils.Utils;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * <p>描述：自定义请求，例如你有自己的ApiService</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/15 17:04 <br>
 * 版本： v1.0<br>
 */
@SuppressWarnings(value={"unchecked", "deprecation"})
public class CustomRequest extends BaseRequest<CustomRequest> {
    public CustomRequest() {
        super("");
    }

    @Override
    public CustomRequest build() {
        return super.build();
    }

    /**
     * 创建api服务  可以支持自定义的api，默认使用BaseApiService,上层不用关心
     *
     * @param service 自定义的apiservice class
     */
    public <T> T create(final Class<T> service) {
        checkvalidate();
        return retrofit.create(service);
    }

    private void checkvalidate() {
        Utils.checkNotNull(retrofit, "请先在调用build()才能使用");
    }

    /**
     * 调用call返回一个Observable<T>
     * 举例：如果你给的是一个Observable<ApiResult<AuthModel>> 那么返回的<T>是一个ApiResult<AuthModel>
     */
    public <T> Observable<T> call(Observable<T> observable) {
        checkvalidate();
        return observable.compose(RxUtil.io_main())
                .compose(new HandleErrTransformer())
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    public <T> void call(Observable<T> observable, CallBack<T> callBack) {
        call(observable, new CallBackSubsciber(context, callBack));
    }

    public <R> void call(Observable observable, Observer<R> subscriber) {
        observable.compose(RxUtil.io_main())
                .subscribe(subscriber);
    }


    /**
     * 调用call返回一个Observable,针对ApiResult的业务<T>
     * 举例：如果你给的是一个Observable<ApiResult<AuthModel>> 那么返回的<T>是AuthModel
     */
    public <T> Observable<T> apiCall(Observable<ApiResult<T>> observable) {
        checkvalidate();
        return observable
                .map(new HandleFuc<T>())
                .compose(RxUtil.<T>io_main())
                .compose(new HandleErrTransformer<T>())
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    public <T> Disposable apiCall(Observable<T> observable, CallBack<T> callBack) {
        return call(observable, new CallBackProxy<ApiResult<T>, T>(callBack) {
        });
    }

    public <T> Disposable call(Observable<T> observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        Observable<CacheResult<T>> cacheobservable = build().toObservable(observable, proxy);
        if (CacheResult.class != proxy.getCallBack().getRawType()) {
            return cacheobservable.compose(new ObservableTransformer<CacheResult<T>, T>() {
                @Override
                public ObservableSource<T> apply(@NonNull Observable<CacheResult<T>> upstream) {
                    return upstream.map(new CacheResultFunc<T>());
                }
            }).subscribeWith(new CallBackSubsciber<T>(context, proxy.getCallBack()));
        } else {
            return cacheobservable.subscribeWith(new CallBackSubsciber<CacheResult<T>>(context, proxy.getCallBack()));
        }
    }

    @SuppressWarnings(value={"unchecked", "deprecation"})
    private <T> Observable<CacheResult<T>> toObservable(Observable observable, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return observable.map(new ApiResultFunc(proxy != null ? proxy.getType() : new TypeToken<ResponseBody>() {
        }.getType()))
                .compose(isSyncRequest ? RxUtil._main() : RxUtil._io_main())
                .compose(rxCache.transformer(cacheMode, proxy.getCallBack().getType()))
                .retryWhen(new RetryExceptionFunc(retryCount, retryDelay, retryIncreaseDelay));
    }

    @Override
    protected Observable<ResponseBody> generateRequest() {
        return null;
    }
}
