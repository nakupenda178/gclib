package com.github.guqt178.utils.event

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * RxBus的kotlin版本
 * @see RxBus
 */
object Bus {
    private val TAG = javaClass.simpleName

    /**
     * Avoid using this property directly, exposed only because it's used in inline fun
     */
    val mBus: Relay<Any> by lazy {
        PublishRelay.create<Any>().toSerialized()
    }

    @JvmStatic
    private fun hasObservers() = mBus.hasObservers()

    /**
     * Subscribes for events of certain type T. Can be called from any thread
     */
    @JvmStatic
    inline fun <reified T : Any> toObservable(): Observable<T> {
        return mBus.ofType(T::class.java)
    }


    @JvmStatic
    fun <T : Any> post(obj: T) {
        mBus.accept(obj)
    }

    /**
     * 使用了lifecycle
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    inline fun <reified T : Any> quickRegiste(
        host: AppCompatActivity,
        crossinline onNext: ((T) -> Unit)
    ) {
        toObservable<T>()
            .subscribeOn(Schedulers.io())
            .compose(RxLifecycleCompact.bind(host).disposeObservableWhen(LifecycleEvent.STOP))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onNext.invoke(it)
            }
    }

    /**
     * 没有使用lifecycle,有可能泄漏
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    inline fun <reified T : Any> quickRegist(crossinline onNext: ((T) -> Unit)) {
        toObservable<T>()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onNext.invoke(it)
            }
    }

}
