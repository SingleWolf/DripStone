package com.walker.dripstone.initializer

import android.content.Context
import androidx.startup.Initializer
import com.walker.core.store.sp.SPHelper
import com.walker.core.store.storage.StorageHelper

class StorageInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        StorageHelper.init(context, "DripStone")
        //SharedPreferences
        SPHelper.init(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}