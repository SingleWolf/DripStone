package com.walker.optimize.summary

import android.content.Context
import android.view.View
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.optimize.group.anr.AnrFragment
import com.walker.optimize.group.caton.CatonFragment
import com.walker.optimize.group.crash.CrashFragment
import com.walker.optimize.group.lancet.LancetFragment
import com.walker.optimize.group.network.NetSpeedFragment
import com.walker.optimize.group.oom.OOMFragment
import com.walker.optimize.group.trace.TraceMethodFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): String? {
        var fragment: String? = when (channelId) {
            TraceMethodFragment.KEY_ID -> {
                TraceMethodFragment.instance().javaClass.name
            }
            NetSpeedFragment.KEY_ID -> {
                NetSpeedFragment.instance().javaClass.name
            }
            CatonFragment.KEY_ID -> {
                CatonFragment.instance().javaClass.name
            }
            AnrFragment.KEY_ID -> {
                AnrFragment.instance().javaClass.name
            }
            CrashFragment.KEY_ID -> {
                CrashFragment.instance().javaClass.name
            }
            OOMFragment.KEY_ID -> {
                OOMFragment.instance().javaClass.name
            }
            LancetFragment.KEY_ID -> {
                LancetFragment.instance().javaClass.name
            }
            else -> null
        }
        return fragment
    }
}