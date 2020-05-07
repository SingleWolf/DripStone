package com.walker.common.arouter.demo

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

interface IDemoProvider : IProvider {
    companion object {
        const val DEMO_ROUTER = "/demo/"
        const val DEMO_SUMMARY_SERVICE = DEMO_ROUTER + "demo_summary"
    }

    fun getSummaryFragment(): Fragment
}