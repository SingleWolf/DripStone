package com.walker.usercenter.login

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.remoteserver.bean.UserInfo
import com.walker.remoteserver.user.IOnUserListener
import com.walker.remoteserver.user.IUserManager
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Author  : walker
 * Date    : 2021/8/23  3:02 下午
 * Email   : feitianwumu@163.com
 * Summary : 服务端模拟器
 */
class ServerMocker(var context: Activity) {

    companion object {
        const val TAG = "ServerMocker"
    }

    var contextRef = WeakReference<Activity?>(null)

    var iUserManager: IUserManager? = null
    var isBind = AtomicBoolean(false)

    init {
        contextRef = WeakReference(context)
    }

    /**
     * 绑定远程服务
     */
    open fun binderRemoteServer() {
        LogHelper.get().i(TAG, "binderRemoteServer")
        val intent = Intent("walker.drip.UserManagerService");
        intent.setPackage("com.walker.remoteserver")
        try {
            contextRef.get()?.bindService(
                intent,
                mConnection,
                Context.BIND_AUTO_CREATE
            )
            isBind.compareAndSet(false, true)
        } catch (e: Throwable) {
            LogHelper.get().e(TAG, e.toString())
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            LogHelper.get().i(TAG, "$name onServiceConnected")
            iUserManager = IUserManager.Stub.asInterface(service)
            try {
                //设置异常监听
                iUserManager?.setOnUserLtener(onUserListener)
                //设置死亡代理
                service.linkToDeath(mDeathRecipient, 0)
            } catch (e: RemoteException) {
                LogHelper.get().e(TAG, e.toString())
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            LogHelper.get().i(TAG, "$name onServiceDisconnected")
            iUserManager = null
            //也可以在此方法中重新绑定远程服务
            //binderRemoteServer();
        }
    }


    /**
     * binder的死亡接收器
     * 当binder死亡时，系统回调binderDied方法，这里移出之前绑定的binder死亡代理，然后重新绑定远程服务
     */
    private val mDeathRecipient: IBinder.DeathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            LogHelper.get().i(TAG, "binderDied")
            if (iUserManager == null) {
                return
            }
            //移出之前绑定的binder死亡代理
            iUserManager?.asBinder()?.unlinkToDeath(this, 0)
            iUserManager = null
            //重新绑定远程服务
            binderRemoteServer()
        }
    }

    /**
     * 异常监听器
     */
    private val onUserListener: IOnUserListener = object : IOnUserListener.Stub() {
        @Throws(RemoteException::class)
        override fun onError(code: Int, error: String) {
            LogHelper.get().i(TAG, error)
            ToastUtils.showCenter(error)
        }
    }

    open fun unBinder() {
        if (!isBind.get()) {
            return
        }
        LogHelper.get().i(TAG, "unBinder")
        if (iUserManager != null && iUserManager?.asBinder()?.isBinderAlive == true) {
            try {
                //移出异常监听
                iUserManager?.removeUserLtener(onUserListener)
                iUserManager?.asBinder()?.unlinkToDeath(mDeathRecipient, 0)
                iUserManager = null
            } catch (e: RemoteException) {
                LogHelper.get().e(TAG, e.toString())
            }
        }
        try {
            contextRef.get()?.unbindService(mConnection)
            isBind.compareAndSet(true, false)
        } catch (e: Exception) {
            LogHelper.get().e(TAG, e.toString())
        }
    }

    open fun login(loginName: String, password: String): UserInfo? {
        return iUserManager?.onLogin(loginName, password)
    }
}