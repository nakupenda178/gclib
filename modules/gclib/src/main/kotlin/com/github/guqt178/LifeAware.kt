package com.github.guqt178

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.CallSuper
import android.support.annotation.NonNull

/**
 * 继承该类感知可以activity生命周期
 */
abstract class LifeAware(@NonNull private val lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onLifeCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onLifeStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onLifeResume() {
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onLifePause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onLifeStop() {
    }


    @CallSuper
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onLifeDestroy() {
        lifecycleOwner.lifecycle.removeObserver(this)
    }
}