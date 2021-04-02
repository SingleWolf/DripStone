package com.walker.optimize.summary

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.optimize.group.anr.AnrFragment
import com.walker.optimize.group.caton.CatonFragment
import com.walker.optimize.group.crash.CrashFragment
import com.walker.optimize.group.network.NetSpeedFragment
import com.walker.optimize.group.oom.OOMFragment
import com.walker.optimize.group.trace.TraceMethodFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): Fragment? {
        var fragment: Fragment? = when (channelId) {
            TraceMethodFragment.KEY_ID -> {
                TraceMethodFragment.instance();
            }
            NetSpeedFragment.KEY_ID -> {
                NetSpeedFragment.instance();
            }
            CatonFragment.KEY_ID -> {
                CatonFragment.instance();
            }
            AnrFragment.KEY_ID -> {
                AnrFragment.instance();
            }
            CrashFragment.KEY_ID -> {
                CrashFragment.instance();
            }
            OOMFragment.KEY_ID -> {
                OOMFragment.instance();
            }
            else -> null
        }
        return fragment
    }
}