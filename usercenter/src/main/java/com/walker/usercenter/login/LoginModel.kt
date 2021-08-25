package com.walker.usercenter.login

import android.app.Activity
import kotlinx.coroutines.delay

class LoginModel {

    companion object {
        const val TAG = "LoginModel"
        const val CODE_SUCCESS = 0;
        const val CODE_INVALID_INFO = 1;
        const val CODE_CONN_ERROR = 2;
        const val CODE_ERROR = 3;
    }

    private var serverMocker: ServerMocker? = null

    suspend fun onLogin(context: Activity, userName: String, password: String): Int {
        serverMocker ?: let {
            serverMocker = ServerMocker(context)
        }
        serverMocker?.binderRemoteServer()
        delay(2000)
        val userInfo = serverMocker?.login(userName, password)
        return if (userInfo != null) {
            if (userInfo.errCode == 1) {
                CODE_SUCCESS
            } else {
                CODE_INVALID_INFO
            }
        } else {
            CODE_CONN_ERROR
        }
    }

    fun execRelease() {
        serverMocker?.unBinder()
        Runtime.getRuntime().gc()
    }

}