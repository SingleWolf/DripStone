package com.walker.demo.summary

import android.content.Context
import android.view.View
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.demo.a2b.AppA2bFragment
import com.walker.demo.floatview.FloatWindowFragment
import com.walker.demo.fmod.VoiceChangeFragment
import com.walker.demo.install.AppInstallFragment
import com.walker.demo.largebitmap.LargeBitmapFragment
import com.walker.demo.location.LocationFragment
import com.walker.demo.paging3.RepoFragment
import com.walker.demo.shortcut.ShortcutFragment
import com.walker.demo.taskflow.TaskFlowFragment
import com.walker.demo.vcard.VCardTestFragment
import com.walker.demo.window.PrePageBitmapFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): String? {
        val fragment: String? = when (channelId) {
            RepoFragment.KEY_ID -> RepoFragment.instance().javaClass.name
            VoiceChangeFragment.KEY_ID -> VoiceChangeFragment.instance().javaClass.name
            AppA2bFragment.KEY_ID -> AppA2bFragment.instance().javaClass.name
            AppInstallFragment.KEY_ID -> AppInstallFragment.instance().javaClass.name
            ShortcutFragment.KEY_ID -> ShortcutFragment.instance().javaClass.name
            LocationFragment.KEY_ID -> LocationFragment.instance().javaClass.name
            TaskFlowFragment.KEY_ID -> TaskFlowFragment.instance().javaClass.name
            LargeBitmapFragment.KEY_ID -> LargeBitmapFragment.instance().javaClass.name
            FloatWindowFragment.KEY_ID -> FloatWindowFragment.instance().javaClass.name
            VCardTestFragment.KEY_ID -> VCardTestFragment.instance().javaClass.name
            PrePageBitmapFragment.KEY_ID -> PrePageBitmapFragment.instance().javaClass.name
            else -> null
        }
        return fragment
    }
}