package com.walker.usercenter.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils

class UserBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        LogHelper.get().i("UserBroadcastReceiver","onReceive()")
        intent?.apply {
            ToastUtils.showCenter(getStringExtra("DATA"))
        }
    }
}