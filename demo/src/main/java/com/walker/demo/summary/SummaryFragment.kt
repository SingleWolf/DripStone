package com.walker.demo.summary

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.walker.common.view.RecycleViewDivider
import com.walker.core.base.mvvm.BaseMvvmFragment
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.demo.R
import com.walker.demo.databinding.FragmentDemoSummaryBinding
import com.walker.common.feedback.FeedbackHelper

@Suppress("DEPRECATION")
class SummaryFragment :
    BaseMvvmFragment<FragmentDemoSummaryBinding, SummaryViewModel, BaseCustomViewModel>() {

    lateinit var summaryAdapter: SummaryRecyclerViewAdapter

    override fun getBindingVariable() = 0

    override fun getLayoutId() = R.layout.fragment_demo_summary

    override fun getViewModel(): SummaryViewModel {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(fragmentTag, SummaryViewModel::class.java)
            return viewModel
        }
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.listview.setHasFixedSize(true)
        viewDataBinding.listview.layoutManager = LinearLayoutManager(context)
        viewDataBinding.listview.addItemDecoration(
            RecycleViewDivider(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )
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
        //FeedbackHelper
        FeedbackHelper.showPop(requireActivity())
    }

    override fun notifyData(sender: ObservableList<BaseCustomViewModel>?) {
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

    override fun getFragmentTag() = "DemoSummaryFragment"

    override fun loadEnd() {
        viewDataBinding.refreshLayout.finishLoadMore()
        viewDataBinding.refreshLayout.finishRefresh()
    }
}