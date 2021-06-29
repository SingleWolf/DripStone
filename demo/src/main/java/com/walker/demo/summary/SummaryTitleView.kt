package com.walker.demo.summary

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.demo.a2b.AppA2bFragment
import com.walker.demo.fmod.VoiceChangeFragment
import com.walker.demo.paging3.RepoFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): Fragment? {
        val fragment: Fragment? = when (channelId) {
            RepoFragment.KEY_ID -> RepoFragment.instance()
            VoiceChangeFragment.KEY_ID -> VoiceChangeFragment.instance()
            AppA2bFragment.KEY_ID -> AppA2bFragment.instance()
            else -> null
        }
        return fragment
    }
}