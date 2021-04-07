package com.walker.dripstone

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.startup.AppInitializer
import com.walker.common.BaseApplication
import com.walker.common.router.IOptimizeRouter
import com.walker.core.log.LogHelper
import com.walker.core.router.RouterLoader
import com.walker.dripstone.activity.SplashActivity
import com.walker.dripstone.initializer.CrashInitializer
import com.walker.network.retrofit.base.RetrofitNetworkApi
import leakcanary.LeakCanary

class GlobalApplication : BaseApplication() {
    override fun onCreate() {
        //setupStrictMode()
        super.onCreate()
        initConfig()
        initPlugin()
        registerActivityLifecycle()
        initOptimize()
    }

    private fun initOptimize() {
        val optimizeProvider = RouterLoader.load(IOptimizeRouter::class.java)
        optimizeProvider?.initBlockCanary()
        optimizeProvider?.transactEpicHooks()
        LeakCanary.config = LeakCanary.config.copy(onHeapAnalyzedListener = LeakUploader())

    }

    private fun initPlugin() {
        pluginLoadPath = "${this.externalCacheDir?.absolutePath}/plugin/pluginTest-debug.apk"
    }

    private fun initConfig() {
        //延迟初始化
        AppInitializer.getInstance(this).initializeComponent(CrashInitializer::class.java)
        //retrofit
        RetrofitNetworkApi.init(NetworkRequestInfo(this))
        setMainPageBlock(::gotoMainPage)
    }

    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                LogHelper.get().d(
                    "registerActivityLifecycle",
                    "${activity::class.java.simpleName} started", true
                )
                activityCount++
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                LogHelper.get().d(
                    "registerActivityLifecycle",
                    "${activity::class.java.simpleName} stopped", true
                )
                activityCount--
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }

    private fun setupStrictMode() {
        if (BuildConfig.DEBUG) {
            //线程检测策略
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads() //读、写操作
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects() //Sqlite对象泄露
                    .detectLeakedClosableObjects() //未关闭的Closable对象泄露
                    .penaltyLog() //违规打印日志
                    .penaltyDeath() //违规崩溃
                    .build()
            )
        }
    }

    fun gotoMainPage(activity: Activity) {
        val intent = Intent(activity, SplashActivity::class.java)
        activity.startActivity(intent)
    }
}