package com.walker.study.summary

import android.content.Context
import android.view.View
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.study.annotation.InjectFragment
import com.walker.study.hotfix.HotfixFragment
import com.walker.study.plugin.HookPluginFragment
import com.walker.study.skin.SkinFragment
import com.walker.study.thread.ThreadFragment
import com.walker.study.webview.WebviewUseFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): String? {
        var fragmentClazz: String? = when (channelId) {
            HotfixFragment.KEY_ID -> HotfixFragment.instance().javaClass.name
            HookPluginFragment.KEY_ID -> HookPluginFragment.instance().javaClass.name
            InjectFragment.KEY_ID -> InjectFragment.instance().javaClass.name
            ThreadFragment.KEY_ID -> ThreadFragment.instance().javaClass.name
            WebviewUseFragment.KEY_ID -> WebviewUseFragment.instance().javaClass.name
            SkinFragment.KEY_ID -> SkinFragment.instance().javaClass.name
            else -> null
        }
        return fragmentClazz
    }
}