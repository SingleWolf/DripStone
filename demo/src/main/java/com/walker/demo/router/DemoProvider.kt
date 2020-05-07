package com.walker.demo.router

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.walker.common.arouter.demo.IDemoProvider
import com.walker.demo.summary.SummaryFragment

@Route(path = IDemoProvider.DEMO_SUMMARY_SERVICE)
class DemoProvider : IDemoProvider {
    override fun init(context: Context?) {

    }

    override fun getSummaryFragment(): Fragment = SummaryFragment()
}