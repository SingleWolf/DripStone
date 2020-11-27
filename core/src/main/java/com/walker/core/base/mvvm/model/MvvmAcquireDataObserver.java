package com.walker.core.base.mvvm.model;

/**
 * @Author Walker
 * @Date 2020-03-18 09:50
 * @Summary mvvm模式下获取数据监听器
 */
public interface MvvmAcquireDataObserver<F> {
    void onSuccess(F t, boolean isFromCache);

    void onFailure(Throwable e);
}
