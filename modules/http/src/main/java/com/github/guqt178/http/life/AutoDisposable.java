//package com.github.guqt178.http.life;
//
//
//import android.util.Log;
//import android.view.View;
//
//import org.reactivestreams.Subscription;
//
//import androidx.lifecycle.Lifecycle;
//import androidx.lifecycle.LifecycleEventObserver;
//import androidx.lifecycle.LifecycleOwner;
//import androidx.lifecycle.LifecycleRegistry;
//import io.reactivex.Flowable;
//import io.reactivex.FlowableConverter;
//import io.reactivex.Observable;
//import io.reactivex.ObservableConverter;
//import io.reactivex.annotations.NonNull;
//import io.reactivex.disposables.CompositeDisposable;
//
//public class AutoDisposable<T> implements ObservableConverter<T, Observable<T>>, FlowableConverter<T, Flowable<T>> {
//
//    private static final String TAG = "AutoDisposable";
//
//    private LifecycleOwner mLifecycleOwner;
//    private CompositeDisposable mDisposable = new CompositeDisposable();
//    private Subscription mSubscription = null;
//
//    public AutoDisposable(@NonNull LifecycleOwner owner) {
//        mLifecycleOwner = owner;
//        initLifecycle();
//    }
//
//    public AutoDisposable(@NonNull View view) {
//        mLifecycleOwner = new ViewLifecycleOwner(view);
//        initLifecycle();
//    }
//
//    private void initLifecycle() {
//        mLifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
//            @Override
//            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
//                if (event == Lifecycle.Event.ON_DESTROY) {
//                    Log.d(TAG, "do ON_DESTROY");
//                    mLifecycleOwner.getLifecycle().removeObserver(this);
//                    mLifecycleOwner = null;
//                    if (mDisposable != null) {
//                        mDisposable.dispose();
//                    }
//                    if (mSubscription != null) {
//                        mSubscription.cancel();
//                    }
//                }
//            }
//        });
//    }
//
//    @Override
//    public Observable<T> apply(@NonNull Observable<T> upstream) {
//        return upstream.filter(this::canCallback).doOnSubscribe(disposable -> {
//            if (mDisposable != null) {
//                mDisposable.add(disposable);
//                Log.d(TAG, "doOnSubscribe:" + mDisposable.size());
//            }
//        });
//    }
//
//    @NonNull
//    @Override
//    public Flowable<T> apply(@NonNull Flowable<T> upstream) {
//        return upstream.filter(this::canCallback).doOnSubscribe(disposable -> {
//            mSubscription = disposable;
//            Log.d(TAG, "doOnSubscribe:" + disposable);
//        });
//    }
//
//
//    private boolean canCallback(T t) {
//        if (mLifecycleOwner == null || mDisposable == null) {
//            Log.d(TAG, "canCallback >> lifecycleOwner or disposable is null");
//            return false;
//        }
//        if (mDisposable.isDisposed()) {
//            Log.d(TAG, "canCallback >> isDisposed");
//            return false;
//        }
//        if (mLifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
//            Log.d(TAG, "canCallback >> DESTROYED");
//            return false;
//        }
//        return true;
//    }
//
//
//    private static class ViewLifecycleOwner implements LifecycleOwner {
//        private LifecycleRegistry lifecycleRegistry;
//
//        private ViewLifecycleOwner(View view) {
//            lifecycleRegistry = new LifecycleRegistry(this);
//            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//                @Override
//                public void onViewAttachedToWindow(View v) {
//                    lifecycleRegistry.setCurrentState(Lifecycle.State.CREATED);
//                }
//
//                @Override
//                public void onViewDetachedFromWindow(View v) {
//                    lifecycleRegistry.setCurrentState(Lifecycle.State.DESTROYED);
//                    view.removeOnAttachStateChangeListener(this);
//                }
//            });
//        }
//
//        @NonNull
//        @Override
//        public Lifecycle getLifecycle() {
//            return lifecycleRegistry;
//        }
//    }
//
//}
