package com.walker.dripstone

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.kingja.loadsir.core.LoadSir
import com.walker.core.exception.CrashHandler
import com.walker.core.log.DefaultLogger
import com.walker.core.log.LogHelper
import com.walker.core.log.LogLevel
import com.walker.core.store.sp.SPHelper
import com.walker.core.store.storage.StorageHelper
import com.walker.core.ui.loadsir.*
import com.walker.core.util.ToastUtils

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initConfig()
    }

    private fun initConfig() {
        //toast
        ToastUtils.init(this)
        //UncaughtExceptionHandler
        CrashHandler.getInstance().init { e -> ToastUtils.showCenter(e.toString()) }
        //SharedPreferences
        SPHelper.init(this)
        //ARouter
        ARouter.init(this)
        ARouter.openDebug()
        ARouter.openLog()
        //LoadSir
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())//添加各种状态页
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
            .addCallback(CustomCallback())
            .setDefaultCallback(LoadingCallback::class.java)//设置默认状态页
            .commit()
        //Storage
        StorageHelper.init(this, "DripStone")
        //Log
        LogHelper.get().setLevel(LogLevel.DEBUG).setLogger(DefaultLogger(this))
            .setExtraLogHandler { tag, log -> ToastUtils.showCenter("$tag->$log") }.config()
    }

}