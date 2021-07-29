package com.walker.collect.cook.cooklist;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
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
import com.walker.core.log.LogHelper;
import com.walker.core.util.DisplayUtils;

import java.lang.reflect.Method;

/**
 * @Author Walker
 * @Date 2020-04-13 23:01
 * @Summary 菜谱大全
 */
public class CookListActivity extends BaseMvvmActivity<ActivityCollectCookListBinding, CookListViewModel> {
    public static final String CHANNEL_ID = "key_101_cook";

    protected CookListRecyclerViewAdapter mAdapter;
    //StaggeredGridLayoutManager.checkForGaps()
    private Method mCheckForGapsMethod;
    //RecyclerView.markItemDecorInsetsDirty()
    private Method mMarkItemDecorInsetsDirtyMethod;

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
        viewDataBinding.listview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    switch (((StaggeredGridLayoutManager.LayoutParams) layoutParams).getSpanIndex()) {
                        case 0:
                            outRect.left = (int) DisplayUtils.dp2px(CookListActivity.this, 15f);
                            outRect.right = (int) DisplayUtils.dp2px(CookListActivity.this, 5f);
                            outRect.bottom = (int) DisplayUtils.dp2px(CookListActivity.this, 5f);
                            outRect.top = (int) DisplayUtils.dp2px(CookListActivity.this, 5f);
                            break;
                        case 1:
                            outRect.left = (int) DisplayUtils.dp2px(CookListActivity.this, 5f);
                            outRect.right = (int) DisplayUtils.dp2px(CookListActivity.this, 15f);
                            outRect.bottom = (int) DisplayUtils.dp2px(CookListActivity.this, 5f);
                            outRect.top = (int) DisplayUtils.dp2px(CookListActivity.this, 5f);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        hookAndHandle();

        mAdapter = new CookListRecyclerViewAdapter();
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

    /**
     * 高性能地解决顶部空白、重排序问题、间距错乱问题
     * <p>
     * 1）反射StaggeredGridLayoutManager的checkForGaps()。在滑动时，仅在有需要的情况下才会重新布局，解决顶部空白、重排序问题
     * 2）反射RecyclerView的markItemDecorInsetsDirty()。如果发生了重排序，刷新ItemDecoration，解决间距错乱问题
     */
    private void hookAndHandle() {
        //禁用动画
        viewDataBinding.listview.setItemAnimator(null);
        try {
            mCheckForGapsMethod = StaggeredGridLayoutManager.class.getDeclaredMethod("checkForGaps");
            mCheckForGapsMethod.setAccessible(true);
            mMarkItemDecorInsetsDirtyMethod = RecyclerView.class.getDeclaredMethod("markItemDecorInsetsDirty");
            mMarkItemDecorInsetsDirtyMethod.setAccessible(true);

            viewDataBinding.listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    try {
                        boolean result = (boolean) mCheckForGapsMethod.invoke(recyclerView.getLayoutManager());
                        if (result) {
                            mMarkItemDecorInsetsDirtyMethod.invoke(recyclerView);
                            LogHelper.get().i("CookListActivity", "发生了重排序，刷新ItemDecoration");
                        }
                    } catch (Exception e) {
                        LogHelper.get().e("CookListActivity", e.getMessage(), true);
                    }
                }
            });
        } catch (Exception e) {
            LogHelper.get().e("CookListActivity", e.getMessage(), true);
        }
    }
}
