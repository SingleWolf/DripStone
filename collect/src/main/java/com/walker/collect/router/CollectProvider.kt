package com.walker.collect.router

import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.walker.collect.summary.SummaryFragment
import com.walker.common.arouter.collect.ICollectProvider

@Route(path = ICollectProvider.COLLECT_SUMMARY_SERVICE)
class CollectProvider : ICollectProvider {
    override fun getHomeFragment(): Fragment =SummaryFragment()

    override fun init(context: Context?) {
    }

}