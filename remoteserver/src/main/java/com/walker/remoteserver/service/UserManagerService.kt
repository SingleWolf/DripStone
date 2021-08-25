package com.walker.remoteserver.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import com.walker.core.log.LogHelper
import com.walker.remoteserver.bean.UserInfo
import com.walker.remoteserver.user.IOnUserListener
import com.walker.remoteserver.user.IUserManager.Stub

/**
 * Author  : walker
 * Date    : 2021/8/13  2:00 下午
 * Email   : feitianwumu@163.com
 * Summary : 用户管理服务
 */
class UserManagerService : Service() {

    companion object {
        const val TAG = "UserManagerService"
    }

    private val userListenerList by lazy { RemoteCallbackList<IOnUserListener>() }
    private val binder = object : Stub() {
        @Throws(RemoteException::class)
        override fun onRegister(userinfo: UserInfo?) {
        }

        @Throws(RemoteException::class)
        override fun onLogin(loginName: String, password: String): UserInfo {
            Thread.sleep(2000)
            var userInfo = UserInfo()
            if (loginName == "walker" && password == "pass1234") {
                userInfo.apply {
                    errCode = 1
                    this.loginName = "walker"
                    this.password="pass1234"
                    this.active=1
                    this.age=18
                }
            } else {
                userInfo.errCode=-1
            }
            return userInfo
        }

        @Throws(RemoteException::class)
        override fun onLogout() {

        }

        @Throws(RemoteException::class)
        override fun setOnUserLtener(listener: IOnUserListener?) {
            listener?.apply {
                userListenerList.register(this)
            }
        }

        @Throws(RemoteException::class)
        override fun removeUserLtener(listener: IOnUserListener?) {
            listener?.apply {
                userListenerList.unregister(this)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        //使用权限验证，仅让通过权限验证的应用可与本服务通信
        val check = checkCallingOrSelfPermission("com.walker.drip.permission.ACCESS_REMOTE_SERVER")
        if (check == PackageManager.PERMISSION_DENIED) {
            //传递未通过权限验证的信息
            LogHelper.get().i(TAG, "no permission")
            return null
        }
        LogHelper.get().i(TAG, "onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        LogHelper.get().i(TAG, "onCreate")
    }

    /**
     * 传递error的信息
     *
     * @param code  编号
     * @param error 信息
     */
    private fun broadcastError(code: Int, error: String) {
        LogHelper.get().i(TAG, "broadcastError($code , $error)")
        val length = userListenerList.beginBroadcast()
        for (pos in 0 until length) {
            val listener = userListenerList.getBroadcastItem(pos)
            listener?.apply {
                try {
                    onError(code, error)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        userListenerList.finishBroadcast()
    }
}