package com.walker.study.summary

import android.content.Context
import android.view.View
import com.walker.common.view.titleview.TitleView
import com.walker.study.activity.ShowActivity

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title)
    }
}