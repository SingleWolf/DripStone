package com.walker.demo.summary

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.demo.paging3.RepoFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): Fragment? {
        val fragment: Fragment? = when (channelId) {
            RepoFragment.KEY_ID -> RepoFragment.instance()
            else -> null
        }
        return fragment
    }
}