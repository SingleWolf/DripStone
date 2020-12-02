package com.walker.common

import android.content.Context
import androidx.multidex.MultiDexApplication

open class BaseApplication : MultiDexApplication() {

    companion object {
        @JvmField
        open var context: Context? = null

        @JvmField
        open var pluginLoadPath: String = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}