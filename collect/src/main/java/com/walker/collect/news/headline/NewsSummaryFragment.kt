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
import com.google.android.material.tabs.TabLayoutMediator
import com.walker.collect.R
import com.walker.collect.databinding.FragmentCollectNewsSummaryBinding

class NewsSummaryFragment : Fragment() {
    companion object {
        const val channel_id = "key_101_news"
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
        //viewpager2
        adapter = HeadlineFragmentAdapter(childFragmentManager, lifecycle)
        viewDataBinding.viewpager.adapter = adapter
        viewDataBinding.viewpager.offscreenPageLimit = 1
        //tablayout
        viewDataBinding.tablayout.tabMode = TabLayout.MODE_AUTO
        var tabLayoutMediator = TabLayoutMediator(
            viewDataBinding.tablayout,
            viewDataBinding.viewpager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab?.text = adapter.getChannelName(position)
            })
        tabLayoutMediator.attach()

        viewModel.dataList.observe(viewLifecycleOwner, object : Observer<ObservableList<Channel>> {
            override fun onChanged(channels: ObservableList<Channel>) {
                synchronized(this) {
                    adapter.setChannels(channels)
                }
            }
        })
        return viewDataBinding.root
    }
}