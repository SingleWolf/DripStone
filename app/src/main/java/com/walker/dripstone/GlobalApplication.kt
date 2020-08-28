package com.walker.dripstone

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.multidex.MultiDexApplication
import androidx.startup.AppInitializer
import com.walker.core.log.LogHelper
import com.walker.core.store.sp.SPHelper
import com.walker.dripstone.initializer.CrashInitializer
import com.walker.network.retrofit.base.RetrofitNetworkApi
import com.walker.platform.push.PushHelper

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
        //延迟初始化
        AppInitializer.getInstance(this).initializeComponent(CrashInitializer::class.java)
        //SharedPreferences
        SPHelper.init(this)
        //retrofit
        RetrofitNetworkApi.init(NetworkRequestInfo(this))

        PushHelper.init(this)
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