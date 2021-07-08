package com.walker.core.base.mvp;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.walker.core.log.LogHelper;

/**
 * @author Walker
 * @date on 2018/7/18 0018 上午 10:45
 * @email feitianwumu@163.com
 * @desc 基于MVP设计模式的activity父类
 */
public abstract class BaseMvpActivity<P extends IBasePresenter> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<P> {

    private final int BASE_LOADER_ID = 1000;

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResId());
        getLoaderManager().initLoader(BASE_LOADER_ID, null, this);//初始化loader
    }

    public abstract IBaseView setAttachView();

    public abstract int setLayoutResId();

    public abstract PresenterLoader<P> setPresenterLoader();

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public Loader<P> onCreateLoader(int id, Bundle args) {
        LogHelper.get().d(this.getClass().getName(), "onCreateLoader() and id = " + id);
        if (id == BASE_LOADER_ID) {
            return setPresenterLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<P> loader, P data) {
        mPresenter = data;
        if (mPresenter != null) {
            mPresenter.attachView(setAttachView());
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
