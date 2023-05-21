package com.walker.ui.summary

import android.content.Context
import android.view.View
import com.walker.common.activity.ShowActivity
import com.walker.common.view.titleview.TitleView
import com.walker.ui.group.arcmenu.ArcMenuFragment
import com.walker.ui.group.colorlayout.ColorLayoutFragment
import com.walker.ui.group.floatlayout.FloatLayoutFragment
import com.walker.ui.group.goodfish.GoodFishFragment
import com.walker.ui.group.photoview.MultiTouchFragment
import com.walker.ui.group.photoview.PhotoViewFragment
import com.walker.ui.group.recyclerview.ceiling.StarFragment
import com.walker.ui.group.recyclerview.draworder.DrawOrderFragment
import com.walker.ui.group.recyclerview.slidecard.SlideCardFragment
import com.walker.ui.group.recyclerview.weblist.WebListFragment

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title, ::genInstance)
    }

    fun genInstance(channelId: String): String? {
        var fragment: String? = when (channelId) {
            FloatLayoutFragment.KEY_ID -> FloatLayoutFragment.instance().javaClass.name
            ColorLayoutFragment.KEY_ID -> ColorLayoutFragment.instance().javaClass.name
            GoodFishFragment.KEY_ID -> GoodFishFragment.instance().javaClass.name
            StarFragment.KEY_ID -> StarFragment.instance().javaClass.name
            PhotoViewFragment.KEY_ID -> PhotoViewFragment.getInstance().javaClass.name
            MultiTouchFragment.KEY_ID -> MultiTouchFragment.getInstance().javaClass.name
            SlideCardFragment.KEY_ID -> SlideCardFragment.getInstance().javaClass.name
            DrawOrderFragment.KEY_ID -> DrawOrderFragment.instance().javaClass.name
            WebListFragment.KEY_ID -> WebListFragment.instance().javaClass.name
            ArcMenuFragment.KEY_ID -> ArcMenuFragment.instance().javaClass.name
            else -> null
        }
        return fragment
    }
}