package com.walker.study.summary

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.study.annotation.InjectFragment
import com.walker.study.hotfix.HotfixFragment
import com.walker.study.plugin.HookPluginFragment
import com.walker.study.thread.ThreadFragment
import com.walker.study.webview.WebviewUseFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): Fragment? {
        var fragment: Fragment? = when (channelId) {
            HotfixFragment.KEY_ID -> HotfixFragment.instance()
            HookPluginFragment.KEY_ID -> HookPluginFragment.instance()
            InjectFragment.KEY_ID -> InjectFragment.instance()
            ThreadFragment.KEY_ID -> ThreadFragment.instance()
            WebviewUseFragment.KEY_ID->WebviewUseFragment.instance()
            else -> null
        }
        return fragment
    }
}