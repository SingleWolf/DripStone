package com.walker.collect.cook.cooklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.walker.collect.R;
import com.walker.collect.databinding.ActivityCollectCookListBinding;
import com.walker.core.base.mvvm.BaseMvvmActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @Author Walker
 * @Date 2020-04-13 23:01
 * @Summary 菜谱大全
 */
public class CookListActivity extends BaseMvvmActivity<ActivityCollectCookListBinding, CookListViewModel> {
    public static final String CHANNEL_ID = "key_101_cook";

    protected CookListRecyclerViewAdapter mAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, CookListActivity.class);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        viewDataBinding.listview.setLayoutManager(layoutManager);
        viewDataBinding.listview.setHasFixedSize(true);
        ((SimpleItemAnimator) viewDataBinding.listview.getItemAnimator()).setSupportsChangeAnimations(false);

        mAdapter=new CookListRecyclerViewAdapter();
        viewDataBinding.listview.setAdapter(mAdapter);

        viewDataBinding.refreshLayout.setRefreshHeader(new WaterDropHeader(this));
        viewDataBinding.refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
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

    private void initToolbar() {
        //Set Toolbar
        setSupportActionBar(viewDataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("菜谱大全");
    }

    @Override
    protected void onRetryBtnClick() {
        viewModel.tryToLoadNextPage();
    }

    @Override
    public void notifyData(ObservableList sender) {
        mAdapter.setData(sender);
        viewDataBinding.refreshLayout.finishLoadMore();
        viewDataBinding.refreshLayout.finishRefresh();
        showSuccess();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected CookListViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get("CookListViewModel", CookListViewModel.class).init();
            return viewModel;
        }
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect_cook_list;
    }

    @Override
    protected void loadEnd() {
        viewDataBinding.refreshLayout.finishLoadMore();
        viewDataBinding.refreshLayout.finishRefresh();
    }
}
