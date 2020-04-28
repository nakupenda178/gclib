package com.github.guqt178.ezhttp.utils;



import com.github.guqt178.ezhttp.func.HandleFuc;
import com.github.guqt178.ezhttp.func.HttpResponseFunc;
import com.github.guqt178.ezhttp.model.ApiResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <p>描述：线程调度工具</p>
 * 作者： zhouyou<br>
 * 日期： 2017/5/15 17:12 <br>
 * 版本： v1.0<br>
 */
public class RxUtil {

    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        HttpLog.i("+++doOnSubscribe+++" + disposable.isDisposed());
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        HttpLog.i("+++doFinally+++");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<ApiResult<T>, T> _io_main() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HandleFuc<T>())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        HttpLog.i("+++doOnSubscribe+++" + disposable.isDisposed());
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        HttpLog.i("+++doFinally+++");
                    }
                })
                .onErrorResumeNext(new HttpResponseFunc<T>());
    }


    public static <T> ObservableTransformer<ApiResult<T>, T> _main() {
        return upstream -> upstream
                //.observeOn(AndroidSchedulers.mainThread())
                .map(new HandleFuc<T>())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        HttpLog.i("+++doOnSubscribe+++" + disposable.isDisposed());
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        HttpLog.i("+++doFinally+++");
                    }
                })
                .onErrorResumeNext(new HttpResponseFunc<T>());
    }
}
