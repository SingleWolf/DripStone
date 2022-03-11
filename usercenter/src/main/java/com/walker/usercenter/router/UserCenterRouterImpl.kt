package com.walker.usercenter.router

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.auto.service.AutoService
import com.walker.common.router.IUserCenterRouter
import com.walker.core.efficiency.activityresult.ActivityResultHelper
import com.walker.usercenter.login.LoginActivity

@AutoService(IUserCenterRouter::class)
class UserCenterRouterImpl : IUserCenterRouter {
    override fun onLogin(activity: Activity) {
        Intent(activity, LoginActivity::class.java).run {
            activity.startActivity(this)
        }
    }

    override fun onLogin(activity: Activity, callback: (code: Int, msg: String) -> Unit) {
        if (activity is FragmentActivity) {
            ActivityResultHelper.init(activity)
                .startActivityForResult(
                    Intent(activity, LoginActivity::class.java)
                ) { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        callback?.invoke(1, "success")
                    } else {
                        callback?.invoke(0, "failed")
                    }
                }
        } else {
            Intent(activity, LoginActivity::class.java).run {
                activity.startActivity(this)
            }
        }
    }
}