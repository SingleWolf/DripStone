package com.walker.collect.news.newslist;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.walker.collect.R;
import com.walker.collect.databinding.FragmentCollectNewsBinding;
import com.walker.common.view.RecycleViewDivider;
import com.walker.core.base.mvvm.BaseMvvmFragment;
import com.walker.core.base.mvvm.customview.BaseCustomViewModel;
import com.walker.core.log.LogHelper;

public class NewsListFragment extends BaseMvvmFragment<FragmentCollectNewsBinding, NewsListViewModel, BaseCustomViewModel> {
    private NewsListRecyclerViewAdapter mAdapter;
    private String mChannelId = "";
    private String mChannelName = "";

    protected final static String KEY_PARAM_CHANNEL_ID = "key_param_channel_id";
    protected final static String KEY_PARAM_CHANNEL_NAME = "key_param_channel_name";

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        LogHelper.get().i("NewsListFragment", String.format("channelId=%s channelName=%s", channelId, channelName));
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect_news;
    }

    @Override
    public NewsListViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(getFragmentTag(), NewsListViewModel.class).init(mChannelId);
            LogHelper.get().e(this.getClass().getSimpleName(), getFragmentTag() + this + ": createViewModel." + viewModel);
            return viewModel;
        }
        return viewModel;
    }

    @Override
    public void notifyData(ObservableList<BaseCustomViewModel> sender) {
        mAdapter.setData(sender);
        viewDataBinding.refreshLayout.finishLoadMore();
        viewDataBinding.refreshLayout.finishRefresh();
        showSuccess();
    }

    @Override
    protected void initParameters() {
        if (getArguments() != null) {
            mChannelId = getArguments().getString(KEY_PARAM_CHANNEL_ID);
            mChannelName = getArguments().getString(KEY_PARAM_CHANNEL_NAME);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDataBinding.listview.setHasFixedSize(true);
        viewDataBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.listview.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL));
        mAdapter = new NewsListRecyclerViewAdapter();
        viewDataBinding.listview.setAdapter(mAdapter);
        viewDataBinding.refreshLayout.setRefreshHeader(new WaterDropHeader(getContext()));
        viewDataBinding.refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.tryToRefresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.tryToLoadNextPage();
            }
        });
        setLoadSir(viewDataBinding.refreshLayout);
        showLoading();
    }

    @Override
    protected void onRetryBtnClick() {
        viewModel.tryToRefresh();
    }

    @Override
    protected String getFragmentTag() {
        return mChannelName;
    }

    @Override
    protected void loadEnd() {

    }

}