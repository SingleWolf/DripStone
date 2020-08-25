package com.walker.dripstone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.walker.core.util.ToastUtils

class ToastInitializer:Initializer<Unit> {
    override fun create(context: Context) {
        ToastUtils.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}