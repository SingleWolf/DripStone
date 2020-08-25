package com.walker.dripstone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.kingja.loadsir.core.LoadSir
import com.walker.core.ui.loadsir.*

class LoadSirInitializer:Initializer<Unit> {
    override fun create(context: Context) {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())//添加各种状态页
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
            .addCallback(CustomCallback())
            .setDefaultCallback(LoadingCallback::class.java)//设置默认状态页
            .commit()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}