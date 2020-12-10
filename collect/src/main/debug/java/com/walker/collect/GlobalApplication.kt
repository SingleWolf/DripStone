package com.walker.collect

import com.kingja.loadsir.core.LoadSir
import com.walker.common.BaseApplication
import com.walker.core.log.DefaultLogger
import com.walker.core.log.LogHelper
import com.walker.core.log.LogLevel
import com.walker.core.store.sp.SPHelper
import com.walker.core.store.storage.StorageHelper
import com.walker.core.ui.loadsir.*
import com.walker.core.util.ToastUtils
import com.walker.network.retrofit.base.RetrofitNetworkApi

class GlobalApplication :BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        //SharedPreferences
        SPHelper.init(this)
        //LoadSir
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())//添加各种状态页
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
            .addCallback(CustomCallback())
            .setDefaultCallback(LoadingCallback::class.java)//设置默认状态页
            .commit()
        //log
        LogHelper.get().setLevel(LogLevel.DEBUG).setLogger(DefaultLogger(this))
            .setExtraLogHandler { tag, log -> ToastUtils.showCenter("$tag->$log") }.config()
        //toast
        ToastUtils.init(this)
        //StorageHelper
        StorageHelper.init(context, "DripStone")
        //retrofit
        RetrofitNetworkApi.init(NetworkRequestInfo(this))
    }
}