package com.walker.common.router

import android.app.Activity

interface IUserCenterRouter {

    fun onLogin(activity: Activity)

    fun onLogin(activity: Activity,callback:(code:Int,msg:String)->Unit)
}