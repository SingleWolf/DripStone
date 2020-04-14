package com.walker.core.base.mvvm;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.walker.core.R;
import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel;
import com.walker.core.base.mvvm.viewmodel.ViewStatus;
import com.walker.core.log.LogHelper;
import com.walker.core.ui.loadsir.EmptyCallback;
import com.walker.core.ui.loadsir.ErrorCallback;
import com.walker.core.ui.loadsir.LoadingCallback;
import com.walker.core.util.ToastUtils;

public abstract class BaseMvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel> extends AppCompatActivity implements Observer {
    protected VM viewModel;
    private LoadService mLoadService;
    protected V viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
        }
        viewModel.dataList.observe(this,this);
        viewModel.viewStatusLiveData.observe(this, this);
    }

    private void initViewModel() {
        viewModel = getViewModel();
    }

    public void setLoadSir(View view) {
        // You can change the callback on sub thread directly.
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
    }

    protected abstract void onRetryBtnClick();

    public abstract void notifyData(ObservableList sender);

    protected abstract VM getViewModel();

    public abstract int getBindingVariable();

    @LayoutRes
    public abstract int getLayoutId();

    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus && mLoadService != null) {
            switch ((ViewStatus) o) {
                case LOADING:
                    mLoadService.showCallback(LoadingCallback.class);
                    break;
                case EMPTY:
                    mLoadService.showCallback(EmptyCallback.class);
                    break;
                case SHOW_CONTENT:
                    mLoadService.showSuccess();
                    break;
                case NO_MORE_DATA:
                    ToastUtils.show(getString(R.string.no_more_data));
                    loadEnd();
                    break;
                case REFRESH_ERROR:
                    if (((ObservableArrayList) viewModel.dataList.getValue()).size() == 0) {
                        mLoadService.showCallback(ErrorCallback.class);
                    } else {
                        String error = viewModel.errorMessage.getValue().toString();
                        ToastUtils.show(error);
                        LogHelper.get().e(getActivityTag(), error, true);
                    }
                    loadEnd();
                    break;
                case LOAD_MORE_FAILED:
                    String failMsg = viewModel.errorMessage.getValue().toString();
                    ToastUtils.show(failMsg);
                    LogHelper.get().e(getActivityTag(), failMsg, true);
                    loadEnd();
                    break;
            }
        }else if(o instanceof ObservableArrayList) {
            notifyData((ObservableList) o);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.get().d(getActivityTag(), "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogHelper.get().d(getActivityTag(), "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.get().d(getActivityTag(), "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogHelper.get().d(getActivityTag(), "onDestroy");
    }

    protected void showSuccess() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    protected abstract void loadEnd();

    protected String getActivityTag() {
        return this.getClass().getSimpleName();
    }
}
