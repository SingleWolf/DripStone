package com.walker.ui.summary

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.walker.core.base.mvvm.BaseMvvmFragment
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.ui.R
import com.walker.ui.databinding.FragmentSummaryBinding


@Suppress("DEPRECATION")
class SummaryFragment :
    BaseMvvmFragment<FragmentSummaryBinding, SummaryViewModel, BaseCustomViewModel>() {

    lateinit var summaryAdapter: SummaryRecyclerViewAdapter

    override fun getBindingVariable() = 0

    override fun getLayoutId() = R.layout.fragment_summary

    override fun getViewModel(): SummaryViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(fragmentTag, SummaryViewModel::class.java)
            Log.e(this::class.java.simpleName, "$fragmentTag: createViewModel.$viewModel")
            return viewModel
        }
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.listview.setHasFixedSize(true)
        viewDataBinding.listview.layoutManager = LinearLayoutManager(context)
        summaryAdapter = SummaryRecyclerViewAdapter()
        viewDataBinding.listview.adapter = summaryAdapter
        viewDataBinding.refreshLayout.setRefreshHeader(WaterDropHeader(context))
        context?.let {
            BallPulseFooter(it).setSpinnerStyle(
                SpinnerStyle.Scale
            )
        }?.let {
            viewDataBinding.refreshLayout.setRefreshFooter(
                it
            )
        }
        viewDataBinding.refreshLayout.setOnRefreshListener { viewModel.tryToRefresh() }
        viewDataBinding.refreshLayout.setOnLoadMoreListener { viewModel.tryToLoadNextPage() }
        setLoadSir(viewDataBinding.refreshLayout)
        showLoading()
    }

    override fun onListItemInserted(sender: ObservableList<BaseCustomViewModel>?) {
        sender?.let {
            summaryAdapter.setData(it)
            viewDataBinding.refreshLayout.finishLoadMore()
            viewDataBinding.refreshLayout.finishRefresh()
            showSuccess()
        }
    }

    override fun onRetryBtnClick() {
        viewModel.tryToRefresh()
    }

    override fun getFragmentTag() = "UISummaryFragment"

    override fun loadEnd() {
        viewDataBinding.refreshLayout.finishLoadMore()
        viewDataBinding.refreshLayout.finishRefresh()
    }
}