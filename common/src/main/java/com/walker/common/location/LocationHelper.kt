package com.walker.common.location

import android.content.Context
import com.walker.common.location.baidu.BaiduLocation
import com.walker.common.location.base.ILocCallback
import com.walker.common.location.base.ILocation
import com.walker.common.location.gaode.GaodeLocation
import com.walker.core.log.LogHelper

object LocationHelper {
    private var locationMap: MutableMap<String, ILocation> = mutableMapOf()
    const val TAG = "LocationHelper"

    fun init(context: Context) {
        addLocation(BaiduLocation(context.applicationContext))
        addLocation(GaodeLocation(context.applicationContext))
    }

    fun addLocation(location: ILocation) {
        location?.apply {
            locationMap[this.getType()] = this
        }
    }

    fun start(mode: String, callback: ILocCallback) {
        val location: ILocation? = locationMap[mode]
        location?.apply {
            setResultListener(callback)
            onStart()
            LogHelper.get().i(TAG,"$mode location is starting")
        } ?: let {
            LogHelper.get().e(TAG,"$mode location is not get")
        }
    }
}