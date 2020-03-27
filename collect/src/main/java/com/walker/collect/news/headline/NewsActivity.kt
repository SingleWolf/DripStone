package com.walker.collect.news.headline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.walker.collect.R
import com.walker.collect.databinding.ActivityCollectNewsBinding

/**
 *@Author Walker
 *
 *@Date   2020-03-27 15:35
 *
 *@Summary 新闻头条
 */
class NewsActivity : AppCompatActivity() {
    private lateinit var adapter: HeadlineFragmentAdapter
    private lateinit var viewDataBinding: ActivityCollectNewsBinding
    var viewModel = HeadlineViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding=DataBindingUtil.setContentView(this,R.layout.activity_collect_news)
        adapter = HeadlineFragmentAdapter(supportFragmentManager)
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
    }
}