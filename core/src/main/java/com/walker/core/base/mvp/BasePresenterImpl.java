package com.walker.core.base.mvp;

/**
 * @date on 2018/7/18 0018 上午 10:44
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  Presenter的父类实现
 */
public class BasePresenterImpl<V extends IBaseView> implements IBasePresenter<V> {

    private V mViewDelegate;

    @Override
    public void attachView(V view) {
        mViewDelegate = view;
    }

    @Override
    public V getAttachView() {
        return mViewDelegate;
    }

    @Override
    public void detachView() {
        mViewDelegate = null;
    }

    @Override
    public void execRelease() {

    }
}