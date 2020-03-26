package com.walker.core.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

/**
 * @date on 2018/7/18 0018 上午 10:45
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  基于MVP设计模式的activity父类
 */
public abstract class BaseMvpActivity<P extends IBasePresenter> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<P> {

    private final int BASE_LOADER_ID = 1000;

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResId());
        getSupportLoaderManager().initLoader(BASE_LOADER_ID,null,this);//初始化loader
    }

    public abstract IBaseView setAttachView();

    public abstract int setLayoutResId();

    public P getPresenter(){
        return mPresenter;
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        mPresenter = data;
        mPresenter.attachView(setAttachView());
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
