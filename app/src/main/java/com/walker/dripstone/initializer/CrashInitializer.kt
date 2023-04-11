package com.walker.dripstone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.walker.core.exception.CrashHandler
import com.walker.core.log.LogHelper

class CrashInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        CrashHandler.getInstance()
            .init(context) { e ->
                val sb = StringBuilder()
                e.stackTrace.forEach {
                    sb.append(it.toString()).append("\n")
                }
                LogHelper.get().e("UncaughtException", sb.toString(), true)
            }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}