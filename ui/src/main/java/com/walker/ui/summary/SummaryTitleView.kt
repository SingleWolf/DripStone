package com.walker.ui.summary

import android.content.Context
import android.view.View
import com.walker.common.view.titleview.TitleView
import com.walker.core.util.ToastUtils
import com.walker.ui.activity.ShowActivity

class SummaryTitleView(context: Context) : TitleView(context) {
    override fun onRootClick(view: View?) {
        ShowActivity.start(context, viewModel.key, viewModel.title)
    }
}