package com.walker.ui.router

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.walker.common.arouter.ui.ISummaryProvider
import com.walker.ui.summary.SummaryFragment

@Route(path = ISummaryProvider.UI_SUMMARY_SERVICE)
class SummaryProvider : ISummaryProvider {
    override fun init(context: Context?) {

    }

    override fun getSummaryFragment(): Fragment = SummaryFragment()
}