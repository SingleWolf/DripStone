package com.walker.core.base.mvp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.walker.core.log.LogHelper;

public abstract class BaseMvpFragment<P extends IBasePresenter> extends Fragment implements LoaderManager.LoaderCallbacks<P> {
    private final int BASE_LOADER_ID = 1100;

    private P mPresenter;

    public Activity mActivity;

    public abstract IBaseView setAttachView();

    public abstract int setLayoutResId();

    public abstract PresenterLoader<P> setPresenterLoader();

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
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
        LogHelper.get().d(this.getClass().getName(), "onLoadFinished()");
        mPresenter = data;
        if (mPresenter != null) {
            mPresenter.attachView(setAttachView());
        }
    }

    @Override
    public void onLoaderReset(Loader<P> loader) {
        LogHelper.get().d(this.getClass().getName(), "onLoaderReset()");
        mPresenter = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogHelper.get().d(this.getClass().getName(), "onCreateView()");
        mActivity.getLoaderManager().initLoader(BASE_LOADER_ID, null, this);
        return inflater.inflate(setLayoutResId(), container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogHelper.get().d(this.getClass().getName(), "onDestroyView()");
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
