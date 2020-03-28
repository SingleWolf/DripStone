package com.walker.collect.summary

import android.content.Context
import android.view.View
import com.walker.collect.activity.ShowActivity
import com.walker.common.view.titleview.TitleView

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        view?.let {
            ShowActivity.start(view.context, viewModel.jumpUri, viewModel.title)
        }
    }
}