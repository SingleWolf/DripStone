package com.walker.core.base.mvp;

/**
 * @date on 2018/7/18 0018 上午 10:31
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  Presenter的父接口
 */
public interface IBasePresenter<V extends IBaseView> {

    void attachView(V view);

    V getAttachView();

    void detachView();

    void execRelease();
}
