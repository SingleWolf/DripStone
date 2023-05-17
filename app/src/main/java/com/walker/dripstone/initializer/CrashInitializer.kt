package com.walker.dripstone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.walker.core.exception.CrashHandler
import com.walker.core.log.LogHelper
import java.util.ArrayList
import java.util.Arrays

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

        val msgAirBagList = mutableListOf<String>()
        msgAirBagList.add("on a null object reference")
        msgAirBagList.add("divide by zero")
        CrashHandler.getInstance().setAirBagConfig(msgAirBagList)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}