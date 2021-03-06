package com.walker.network.retrofit.observer;

import com.walker.core.base.mvvm.model.MvvmAcquireDataObserver;
import com.walker.core.base.mvvm.model.MvvmBaseModel;
import com.walker.network.retrofit.errorhandler.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseMvvmObserver<T> implements Observer<T> {

    MvvmBaseModel baseModel;
    MvvmAcquireDataObserver<T> mvvmNetworkObserver;

    public BaseMvvmObserver(MvvmBaseModel baseModel, MvvmAcquireDataObserver<T> mvvmNetworkObserver) {
        this.baseModel = baseModel;
        this.mvvmNetworkObserver = mvvmNetworkObserver;
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ExceptionHandle.ResponseThrowable) {
            mvvmNetworkObserver.onFailure(e);
        } else {
            ExceptionHandle.ResponseThrowable responseThrowable = new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN);
            responseThrowable.message = "error";
            mvvmNetworkObserver.onFailure(responseThrowable);
        }
    }

    @Override
    public void onNext(T t) {
        mvvmNetworkObserver.onSuccess(t, false);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (baseModel != null) {
            baseModel.addDisposable(d);
        }
    }

    @Override
    public void onComplete() {
    }
}
