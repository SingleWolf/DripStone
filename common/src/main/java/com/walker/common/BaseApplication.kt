package com.walker.common

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDexApplication

open class BaseApplication : MultiDexApplication() {
    companion object {
        @JvmField
        open var context: Context? = null

        @JvmField
        open var application: Application? = null

        var activityCount: Int = 0

        @JvmField
        open var pluginLoadPath: String = ""

        @JvmField
        var taskCollection = LinkedHashMap<String, (activity: Activity) -> Any>()

        open fun setMainPageBlock(task: (activity: Activity) -> Any) {
            taskCollection["MainPage"] = task
        }

        open fun gotoMainPage(activity: Activity) {
            taskCollection["MainPage"]?.invoke(activity)
        }

        open fun isAppBack(): Boolean {
            return activityCount <= 0
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        application = this
    }
}