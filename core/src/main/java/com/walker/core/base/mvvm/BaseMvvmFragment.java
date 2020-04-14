package com.walker.core.base.mvvm;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
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

public abstract class BaseMvvmFragment<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends Fragment implements Observer {
    protected VM viewModel;
    protected V viewDataBinding;
    private LoadService mLoadService;

    public abstract int getBindingVariable();

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract VM getViewModel();

    public abstract void notifyData(ObservableList<D> sender);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onCreate",true);
        initParameters();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onCreateView",true);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onViewCreated",true);
        viewModel = getViewModel();
        getLifecycle().addObserver(viewModel);
        viewModel.dataList.observe(getViewLifecycleOwner(), this);
        viewModel.viewStatusLiveData.observe(getViewLifecycleOwner(), this);
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }
    }

    /***
     *   初始化参数
     */
    protected void initParameters() {
    }

    protected abstract void onRetryBtnClick();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onDetach");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        LogHelper.get().d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:"+this + ": " + "onDestroyView");
        super.onDestroyView();
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

    protected abstract String getFragmentTag();

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
                    if (((ObservableArrayList)viewModel.dataList.getValue()).size() == 0) {
                        mLoadService.showCallback(ErrorCallback.class);
                    } else {
                        String error = viewModel.errorMessage.getValue().toString();
                        ToastUtils.show(error);
                        LogHelper.get().e(getFragmentTag(), error, true);
                    }
                    loadEnd();
                    break;
                case LOAD_MORE_FAILED:
                    String failMsg = viewModel.errorMessage.getValue().toString();
                    ToastUtils.show(failMsg);
                    LogHelper.get().e(getFragmentTag(), failMsg, true);
                    loadEnd();
                    break;
            }
        } else if(o instanceof ObservableArrayList) {
            notifyData((ObservableArrayList<D>)o);
        }
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
}
