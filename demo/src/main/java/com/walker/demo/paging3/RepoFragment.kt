package com.walker.demo.paging3

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walker.common.view.RecycleViewDivider
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepoFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_paging3_fragment"

        fun instance() = RepoFragment()
    }

    private val viewModel by lazy { ViewModelProvider(this).get(RepoViewModel::class.java) }
    private val repoAdapter = RepoAdapter()

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_view)
        val progressBar = baseView.findViewById<ProgressBar>(R.id.progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(holdContext)
        recyclerView.addItemDecoration( RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL))
        recyclerView.adapter =
            repoAdapter.withLoadStateFooter(FooterAdapter { repoAdapter.retry() })
        lifecycleScope.launch {
            viewModel.getPagingData().collect { pagingData ->
                repoAdapter.submitData(pagingData)
            }
        }
        repoAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }
                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    progressBar.visibility = View.INVISIBLE
                    LogHelper.get().d("RepoFragment", state.error.message)
                    ToastUtils.show("Load Error: ${state.error.message}")
                }
            }
        } }

    override fun getLayoutId() = R.layout.fragment_demo_repo
}