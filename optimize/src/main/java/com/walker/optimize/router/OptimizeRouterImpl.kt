package com.walker.optimize.router

import androidx.fragment.app.Fragment
import com.google.auto.service.AutoService
import com.walker.common.router.IOptimizeRouter
import com.walker.optimize.group.caton.blockcanary.BlockCanary
import com.walker.optimize.group.epic.EpicHookHelper
import com.walker.optimize.summary.SummaryFragment

@AutoService(IOptimizeRouter::class)
class OptimizeRouterImpl : IOptimizeRouter {
    override fun getSummaryFragment(): Fragment? {
        return SummaryFragment()
    }

    override fun initBlockCanary() {
        BlockCanary.install()
    }

    override fun transactEpicHooks() {
        EpicHookHelper.get().hookThreads()

        EpicHookHelper.get().hookDexLoad()

        EpicHookHelper.get().hookImageView()
    }
}