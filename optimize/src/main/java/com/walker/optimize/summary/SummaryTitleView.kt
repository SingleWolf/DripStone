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
                TraceMethodFragment.instance().javaClass.simpleName
            }
            NetSpeedFragment.KEY_ID -> {
                NetSpeedFragment.instance().javaClass.simpleName
            }
            CatonFragment.KEY_ID -> {
                CatonFragment.instance().javaClass.simpleName
            }
            AnrFragment.KEY_ID -> {
                AnrFragment.instance().javaClass.simpleName
            }
            CrashFragment.KEY_ID -> {
                CrashFragment.instance().javaClass.simpleName
            }
            OOMFragment.KEY_ID -> {
                OOMFragment.instance().javaClass.simpleName
            }
            LancetFragment.KEY_ID -> {
                LancetFragment.instance().javaClass.simpleName
            }
            else -> null
        }
        return fragment
    }
}