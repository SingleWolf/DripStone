package com.walker.demo.router

import androidx.fragment.app.Fragment

import com.google.auto.service.AutoService
import com.walker.common.router.IDemoRouter
import com.walker.demo.summary.SummaryFragment

@AutoService(IDemoRouter::class)
class DemoRouterImpl : IDemoRouter {
    override fun getSummaryFragment(): Fragment? {
        return SummaryFragment()
    }
}
