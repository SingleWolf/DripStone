package com.walker.dripstone

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import com.kingja.loadsir.core.LoadSir
import com.walker.core.exception.CrashHandler
import com.walker.core.log.DefaultLogger
import com.walker.core.log.LogHelper
import com.walker.core.log.LogLevel
import com.walker.core.store.sp.SPHelper
import com.walker.core.store.storage.StorageHelper
import com.walker.core.ui.loadsir.*
import com.walker.core.util.ToastUtils
import com.walker.network.retrofit.base.RetrofitNetworkApi

class GlobalApplication : MultiDexApplication() {

    companion object{
        var activityCount: Int = 0
    }

    override fun onCreate() {
        super.onCreate()
        initConfig()
        registerActivityLifecycle()
    }

    private fun initConfig() {
        //toast
        ToastUtils.init(this)
        //UncaughtExceptionHandler
        CrashHandler.getInstance()
            .init { e -> LogHelper.get().e("UncaughtException", e.toString(), true) }
        //SharedPreferences
        SPHelper.init(this)
        //Log
        LogHelper.get().setLevel(LogLevel.DEBUG).setLogger(DefaultLogger(this))
            .setExtraLogHandler { tag, log -> ToastUtils.showCenter("$tag->$log") }.config()
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
        //retrofit
        RetrofitNetworkApi.init(NetworkRequestInfo(this))
    }

    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                LogHelper.get().d("registerActivityLifecycle","${activity::class.java.simpleName} started")
                activityCount++
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                LogHelper.get().d("registerActivityLifecycle","${activity::class.java.simpleName} stopped")
                activityCount--
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }
}