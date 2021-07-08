package com.walker.usercenter.router

import android.app.Activity
import android.content.Intent
import com.google.auto.service.AutoService
import com.walker.common.router.IUserCenterRouter
import com.walker.usercenter.login.LoginActivity

@AutoService(IUserCenterRouter::class)
class UserCenterRouterImpl : IUserCenterRouter {
    override fun onLogin(activity: Activity) {
        Intent(activity, LoginActivity::class.java).run {
            activity.startActivity(this)
        }
    }
}