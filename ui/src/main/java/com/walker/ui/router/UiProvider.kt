package com.walker.ui.router

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.walker.common.arouter.ui.IUiProvider
import com.walker.ui.summary.SummaryFragment

@Route(path = IUiProvider.UI_SUMMARY_SERVICE)
class UiProvider : IUiProvider {
    override fun init(context: Context?) {

    }

    override fun getSummaryFragment(): Fragment = SummaryFragment()
}