package com.walker.common.arouter.collect

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider

interface ICollectProvider : IProvider {
    companion object {
        const val COLLECT_ROUTER = "/collect/"
        const val COLLECT_SUMMARY_SERVICE = COLLECT_ROUTER + "collect_summary"
    }

    fun getHomeFragment(): Fragment
}