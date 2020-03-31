package com.walker.network.retrofit.observer;

import com.walker.network.retrofit.errorhandler.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseCommonObserver<T> implements Observer<T> {

    private AcquireDataObserver acquireDataObserver;

    public interface AcquireDataObserver<F> {
        void onSuccess(F t);

        void onFailure(Throwable e);
    }

    public BaseCommonObserver(AcquireDataObserver acquireDataObserver) {
        this.acquireDataObserver = acquireDataObserver;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        acquireDataObserver.onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ExceptionHandle.ResponseThrowable) {
            acquireDataObserver.onFailure(e);
        } else {
            acquireDataObserver.onFailure(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onComplete() {

    }
}
