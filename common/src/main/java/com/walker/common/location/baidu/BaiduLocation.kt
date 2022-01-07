package com.walker.common.location.baidu

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.walker.common.location.base.ILocCallback
import com.walker.common.location.base.ILocation
import com.walker.common.location.base.LocConstant
import com.walker.core.log.LogHelper
import java.lang.ref.WeakReference

/**
 * Author  : walker
 * Date    : 2021/12/22  2:35 下午
 * Email   : feitianwumu@163.com
 * Summary : 百度地图定位
 */
class BaiduLocation(val context: Context) : ILocation, BDAbstractLocationListener() {

    private var client: LocationClient? = null
    private var locOption: LocationClientOption? = null
    private var listenerMap = linkedSetOf<WeakReference<ILocCallback>>()

    companion object {
        const val KEY = "Baidu"
        const val TAG = "BaiduLocation"
    }

    override fun onStart() {
        client ?: let {
            try {
                client =
                    LocationClient(context.applicationContext, getDefaultLocationClientOption())
                client?.registerLocationListener(this)
            } catch (e: Throwable) {
                LogHelper.get().e(TAG, "init error=$e")
            }
        }
        client?.apply {
            try {
                this.start()
                LogHelper.get().i(TAG, "start()")
            } catch (e: Throwable) {
                LogHelper.get().e(TAG, "onStart error=$e")
            }
        }
    }

    override fun onStop() {
        client?.apply {
            if (this.isStarted) {
                try {
                    this.stop()
                    LogHelper.get().i(TAG, "stop()")
                } catch (e: Throwable) {
                    LogHelper.get().e(TAG, "onStop error=$e")
                }
            }
        }
    }

    override fun onDestroy() {
        client = null
    }

    override fun setResultListener(listener: ILocCallback) {
        listener?.apply {
            listenerMap.add(WeakReference(this))
        }
    }

    override fun getType() = KEY

    private fun getDefaultLocationClientOption(): LocationClientOption? {
        locOption ?: let {
            LocationClientOption().apply {
                locationMode =
                    LocationClientOption.LocationMode.Hight_Accuracy // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
                setCoorType("bd09ll") // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
                setScanSpan(3000) // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
                setIsNeedAddress(true) // 可选，设置是否需要地址信息，默认不需要
                setIsNeedLocationDescribe(false) // 可选，设置是否需要地址描述
                setNeedDeviceDirect(false) // 可选，设置是否需要设备方向结果
                isLocationNotify = false // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
                setIgnoreKillProcess(true) // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop
                setIsNeedLocationDescribe(false) // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
                setIsNeedLocationPoiList(false) // 可选，默认false，设置是否需要POI结果，可以在BDLocation
                SetIgnoreCacheException(false) // 可选，默认false，设置是否收集CRASH信息，默认收集
                isOpenGps = true // 可选，默认false，设置是否开启Gps定位
                setIsNeedAltitude(false) // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
                locOption = this
            }
        }
        return locOption
    }

    override fun onReceiveLocation(location: BDLocation?) {
        LogHelper.get().i(TAG, "onReceiveLocation()")
        val data = mutableMapOf<String, String>()
        data[LocConstant.KEY_TYPE] = getType()
        if (location != null) {
            try {
                if (location.locType != BDLocation.TypeServerError) {
                    data[LocConstant.KEY_SUC] = LocConstant.CODE_SUC
                    data[LocConstant.ERR_CODE] = location.locType.toString()
                    data[LocConstant.KEY_LATITUDE] = location.latitude.toString()
                    data[LocConstant.KEY_LONGTITUDE] = location.longitude.toString()
                    data[LocConstant.KEY_COUNTRY] = location.country
                    data[LocConstant.KEY_PROVINCE] = location.province
                    data[LocConstant.KEY_CITY] = location.city
                    data[LocConstant.KEY_ADDR] = location.addrStr
                } else {
                    data[LocConstant.KEY_SUC] = LocConstant.CODE_FAIL
                    data[LocConstant.ERR_CODE] = location.locType.toString()
                }
            } catch (e: Throwable) {
                LogHelper.get().e(TAG, "get loc error=$e", true)
            }
        } else {
            data[LocConstant.KEY_SUC] = LocConstant.CODE_FAIL
            data[LocConstant.ERR_CODE] = "0000"
        }
        LogHelper.get().i(TAG, "onReceiveLocation() : $data", true)

        //回传结果
        listenerMap.forEach {
            it.get()?.apply {
                this.onResult(data)
                //仅定位一次
                if (!this.isKeep()) {
                    listenerMap.remove(it)
                    LogHelper.get().i(TAG, "移除回调接口")
                }
            } ?: apply {
                listenerMap.remove(it)
                LogHelper.get().i(TAG, "移除回调接口")
            }
        }

        if (listenerMap.isEmpty()) {
            onStop()
        }
    }
}