package com.walker.ui.router

import androidx.fragment.app.Fragment

import com.google.auto.service.AutoService
import com.walker.common.router.IUiRouter
import com.walker.ui.summary.SummaryFragment

@AutoService(IUiRouter::class)
class UiRouterImpl : IUiRouter {

    override fun getSummaryFragment(): Fragment? {
        return SummaryFragment()
    }
}
