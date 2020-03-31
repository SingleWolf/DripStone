package com.walker.dripstone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData

class NetworkState(val context: Context) : LiveData<Boolean>() {
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            try {
                //通过getSystemService()方法得到connectionManager这个系统服务类，专门用于管理网络连接
                val connectionManager: ConnectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectionManager.activeNetworkInfo
                if (networkInfo != null && networkInfo.isAvailable) {
                    postValue(true)
                } else {
                    postValue(false)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onActive() {
        super.onActive()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(broadcastReceiver)
    }
}