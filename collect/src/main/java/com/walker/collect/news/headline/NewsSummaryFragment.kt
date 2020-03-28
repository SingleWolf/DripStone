package com.walker.collect.news.headline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.walker.collect.R
import com.walker.collect.databinding.FragmentCollectNewsSummaryBinding

class NewsSummaryFragment : Fragment() {
    companion object{
        const val channel_id="key_101_news"
    }

    private lateinit var adapter: HeadlineFragmentAdapter
    private lateinit var viewDataBinding: FragmentCollectNewsSummaryBinding
    var viewModel = HeadlineViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_collect_news_summary,
            container,
            false
        )
        adapter = HeadlineFragmentAdapter(childFragmentManager)
        viewDataBinding.tablayout.tabMode = TabLayout.MODE_AUTO
        viewDataBinding.viewpager.adapter = adapter
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager)
        viewDataBinding.viewpager.offscreenPageLimit = 1
        viewModel.dataList.observe(this, object : Observer<ObservableList<Channel>> {
            override fun onChanged(channels: ObservableList<Channel>) {
                synchronized(this) {
                    adapter.setChannels(channels)
                }
            }
        })
        return viewDataBinding.root
    }
}