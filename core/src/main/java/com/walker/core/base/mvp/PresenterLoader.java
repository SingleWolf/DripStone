package com.walker.core.base.mvp;

import android.content.Context;

import androidx.loader.content.Loader;

/**
 * @date on 2018/7/18 0018 上午 10:34
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  利用系统提供的loader以延长presenter的生命周期
 */
public class PresenterLoader<P extends IBasePresenter> extends Loader<P> {
    private final PresenterFactory<P> mFactory;
    private P mPresenter;

    public PresenterLoader(Context context, PresenterFactory<P> factory) {
        super(context);
        mFactory = factory;
    }

    /**
     * 在Activity的onStart()调用之后
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mPresenter != null){
            deliverResult(mPresenter);//会将Presenter传递给Activity/Fragment。
            return;
        }
        forceLoad();
    }

    /**
     * 在调用forceLoad()方法后自动调用，我们在这个方法中创建Presenter并返回它。
     */
    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mPresenter = mFactory.create();//创建presenter
        deliverResult(mPresenter);
    }

    /**
     * 会在Loader被销毁之前调用，我们可以在这里告知Presenter以终止某些操作或进行清理工作。
     */
    @Override
    protected void onReset() {
        super.onReset();
        mPresenter.execRelease();
        mPresenter = null;
    }
}
