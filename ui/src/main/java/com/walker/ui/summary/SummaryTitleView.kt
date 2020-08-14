package com.walker.ui.summary

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.ui.group.colorlayout.ColorLayoutFragment
import com.walker.ui.group.floatlayout.FloatLayoutFragment
import com.walker.ui.group.goodfish.GoodFishFragment
import com.walker.ui.group.photoview.PhotoViewFragment
import com.walker.ui.group.recyclerview.StarFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): Fragment? {
        var fragment: Fragment? = when (channelId) {
            FloatLayoutFragment.KEY_ID -> FloatLayoutFragment.instance()
            ColorLayoutFragment.KEY_ID -> ColorLayoutFragment.instance()
            GoodFishFragment.KEY_ID -> GoodFishFragment.instance()
            StarFragment.KEY_ID -> StarFragment.instance()
            PhotoViewFragment.KEY_ID -> PhotoViewFragment.getInstance()
            else -> null
        }
        return fragment
    }
}