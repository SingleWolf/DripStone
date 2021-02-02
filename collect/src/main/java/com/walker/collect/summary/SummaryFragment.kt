package com.walker.collect.summary

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.walker.collect.R
import com.walker.collect.databinding.FragmentCollectSummaryBinding
import com.walker.common.view.RecycleViewDivider
import com.walker.core.base.mvvm.BaseMvvmFragment
import com.walker.core.base.mvvm.customview.BaseCustomViewModel

@Suppress("DEPRECATION")
class SummaryFragment :
    BaseMvvmFragment<FragmentCollectSummaryBinding, SummaryViewModel, BaseCustomViewModel>() {

    lateinit var summaryAdapter: SummaryRecyclerViewAdapter

    override fun getBindingVariable() = 0

    override fun getLayoutId() = R.layout.fragment_collect_summary

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
        viewDataBinding.listview.addItemDecoration(
            RecycleViewDivider(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )
        summaryAdapter = SummaryRecyclerViewAdapter()
        viewDataBinding.listview.adapter = summaryAdapter
        viewDataBinding.refreshLayout.isEnableLoadMore = false
        viewDataBinding.refreshLayout.isEnableRefresh=false
        setLoadSir(viewDataBinding.refreshLayout)
        showLoading()
    }

    override fun notifyData(sender: ObservableList<BaseCustomViewModel>?) {
        sender?.let {
            summaryAdapter.setData(it)
            showSuccess()
        }
    }

    override fun onRetryBtnClick() {
        viewModel.tryToRefresh()
    }

    override fun getFragmentTag() = "CollectSummaryFragment"

    override fun loadEnd() {
    }
}