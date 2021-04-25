package com.walker.common.router

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment

/**
 *@Author Walker
 *
 *@Date   2020-08-11 11:06
 *
 *@Summary Study模块路由
 */
interface IStudyRouter {
    fun getSummaryFragment(): Fragment?

    fun loadClass(context: Context, loadPath: String)

    fun initSkin(application: Application)
}