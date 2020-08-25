package com.walker.dripstone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.walker.core.log.DefaultLogger
import com.walker.core.log.LogHelper
import com.walker.core.log.LogLevel
import com.walker.core.util.ToastUtils

class LogInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        LogHelper.get().setLevel(LogLevel.DEBUG).setLogger(DefaultLogger(context))
            .setExtraLogHandler { tag, log -> ToastUtils.showCenter("$tag->$log") }.config()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}