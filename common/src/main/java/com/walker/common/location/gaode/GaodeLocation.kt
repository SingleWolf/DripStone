package com.walker.common.location.gaode

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.setLocationProtocol
import com.amap.api.location.AMapLocationListener
import com.walker.common.location.base.ILocCallback
import com.walker.common.location.base.ILocation
import com.walker.common.location.base.LocConstant
import com.walker.core.log.LogHelper
import java.lang.ref.WeakReference

/**
 * Author  : walker
 * Date    : 2021/12/22  2:35 下午
 * Email   : feitianwumu@163.com
 * Summary : 高德地图定位
 */
class GaodeLocation(val context: Context) : ILocation, AMapLocationListener {

    private var client: AMapLocationClient? = null
    private var locOption: AMapLocationClientOption? = null
    private var listenerMap = linkedSetOf<WeakReference<ILocCallback>>()

    companion object {
        const val KEY = "Gaode"
        const val TAG = "GaodeLocation"
    }

    override fun onStart() {
        client ?: let {
            try {
                AMapLocationClient.updatePrivacyShow(context, true, true);
                AMapLocationClient.updatePrivacyAgree(context, true);
                client = AMapLocationClient(context.applicationContext)
                client?.setLocationOption(getDefaultLocationClientOption())
                client?.setLocationListener(this)
            } catch (e: Throwable) {
                LogHelper.get().e(TAG, "init error=$e")
            }
        }
        client?.apply {
            try {
                this.startLocation()
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
                    this.stopLocation()
                    LogHelper.get().i(TAG, "stop()")
                } catch (e: Throwable) {
                    LogHelper.get().e(TAG, "onStop error=$e")
                }
            }
        }
    }

    override fun onDestroy() {
        client?.onDestroy()
    }

    override fun setResultListener(listener: ILocCallback) {
        listener?.apply {
            listenerMap.add(WeakReference(this))
        }
    }

    override fun getType() = KEY

    private fun getDefaultLocationClientOption(): AMapLocationClientOption? {
        locOption ?: let {
            AMapLocationClientOption().apply {
                locationMode =
                    AMapLocationClientOption.AMapLocationMode.Hight_Accuracy //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
                isGpsFirst = false //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
                httpTimeOut = 10000 //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
                interval = 3000 //可选，设置定位间隔。默认为2秒
                isNeedAddress = true //可选，设置是否返回逆地理地址信息。默认是true
                isOnceLocation = false //可选，设置是否单次定位。默认是false
                isOnceLocationLatest =
                    false //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
                setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP) //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
                isSensorEnable = false //可选，设置是否使用传感器。默认是false
                isWifiScan =
                    true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
                isLocationCacheEnable = false //可选，设置是否使用缓存定位，默认为true
                geoLanguage =
                    AMapLocationClientOption.GeoLanguage.DEFAULT //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
            }
        }
        return locOption
    }

    override fun onLocationChanged(location: AMapLocation?) {
        LogHelper.get().i(TAG, "onLocationChanged()")
        val data = mutableMapOf<String, String>()
        data[LocConstant.KEY_TYPE] = getType()
        if (location != null && location.errorCode == 0) {
            data[LocConstant.KEY_SUC] = LocConstant.CODE_SUC
            try {
                data[LocConstant.KEY_LATITUDE] = location.latitude.toString()
                data[LocConstant.KEY_LONGTITUDE] = location.longitude.toString()
                data[LocConstant.KEY_COUNTRY] = location.country
                data[LocConstant.KEY_PROVINCE] = location.province
                data[LocConstant.KEY_CITY] = location.city
                data[LocConstant.KEY_ADDR] = location.address
            } catch (e: Throwable) {
                LogHelper.get().e(TAG, "get loc error=$e")
            }
        } else {
            data[LocConstant.KEY_SUC] = LocConstant.CODE_FAIL
        }

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