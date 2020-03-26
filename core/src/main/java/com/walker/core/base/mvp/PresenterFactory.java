package com.walker.core.base.mvp;

/**
 * @date on 2018/7/18 0018 上午 10:33
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  Presenter的生产接口
 */
public interface PresenterFactory<P extends IBasePresenter> {
    P create();//创建Presenter
}
