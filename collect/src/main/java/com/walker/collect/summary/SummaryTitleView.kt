package com.walker.collect.summary

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.walker.collect.activity.ShowActivity
import com.walker.collect.cook.cooklist.CookListActivity
import com.walker.common.view.titleview.TitleView

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        view?.let {
            if (TextUtils.equals(CookListActivity.CHANNEL_ID, viewModel.key)) {
                CookListActivity.start(view.context)
            } else {
                ShowActivity.start(view.context, viewModel.jumpUri, viewModel.title)
            }
        }
    }
}