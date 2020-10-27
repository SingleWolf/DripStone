package com.walker.optimize.summary

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.optimize.group.network.NetSpeedFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): Fragment? {
        var fragment: Fragment? = when (channelId) {
            NetSpeedFragment.KEY_ID->{
                NetSpeedFragment.instance();
            }
            else -> null
        }
        return fragment
    }
}