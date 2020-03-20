package com.walker.dripstone

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.kingja.loadsir.core.LoadSir
import com.walker.core.store.sp.SPHelper
import com.walker.core.ui.loadsir.*
import com.walker.core.util.ToastUtils

class GlobalApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        initConfig()
    }

    private fun initConfig() {
        SPHelper.init(this)
        ToastUtils.init(this)
        ARouter.init(this)
        ARouter.openDebug()
        ARouter.openLog()
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())//添加各种状态页
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
            .addCallback(CustomCallback())
            .setDefaultCallback(LoadingCallback::class.java)//设置默认状态页
            .commit()
    }

}