package com.walker.common.router

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * @Author Walker
 * @Date 2020/10/23 4:04 PM
 * @Summary 优化模块路由
 */
interface IOptimizeRouter {
    fun getSummaryFragment(): Fragment?

    fun init(context :Context)

    fun transactEpicHooks()
}