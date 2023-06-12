package com.walker.demo.feedback

import android.Manifest
import android.app.Activity
import android.view.MotionEvent
import androidx.fragment.app.FragmentActivity
import com.lzf.easyfloat.EasyFloat
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.common.activity.ShowActivity
import com.walker.common.feedback.IPlayerCapacity
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.common.window.OnOrientationListener
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.window.PrePageBitmapFragment

class FeedbackPlayerImpl : IPlayerCapacity, OnOrientationListener {

    private var floatView: FeedbackLogoFloat? = null
    private val floatViewAdapter by lazy { FeedbackLogoFloatAdapter() }

    override fun showPop(activity: Activity) {
        PermissionX.init(activity as FragmentActivity)
            .permissions(
                Manifest.permission.SYSTEM_ALERT_WINDOW
            )
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "该功能需要以下权限",
                    "允许",
                    "拒绝"
                )
            })
            .request(
                RequestCallback { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        startFloatWindow(activity)
                    }
                }
            )
    }

    override fun dismissPop() {
        onDismissFloatWindow()
    }

    override fun transact() {
    }

    override fun getCurrentFlag() = ""
    override fun dispatchTouchEventFromScreen(event: MotionEvent) {
        if (EasyFloat.isShow(FeedbackTransactFloat.TAG)) {
            LogHelper.get().i("FeedbackPlayerImpl", "监测到屏幕滑动，收起业务处理悬浮框")
            EasyFloat.dismiss(FeedbackTransactFloat.TAG)
            EasyFloat.show(FeedbackLogoFloat.TAG)
        }
    }

    fun startFloatWindow(activity: Activity) {
        floatViewAdapter.setCallback<Int>(object : FloatViewAdapter.OnCallback {
            override fun <Int> callback(value: Int) {
                //transact()
                if (value == FeedbackLogoFloatAdapter.CODE_CLICK_REPORT) {
                    floatView?.apply {
                        ShowActivity.start(
                            context,
                            PrePageBitmapFragment.KEY_ID,
                            "PrePageBitmapFragment",
                            ::genInstance
                        )
                    }
                } else if (value == FeedbackLogoFloatAdapter.CODE_CLICK_BUSINESS) {
                    ToastUtils.showCenter("业务办理")
                }
            }

        })
        if (floatView == null) {
            floatView = FeedbackLogoFloat(activity)
        }
        floatView?.apply {
            setAdapter(floatViewAdapter)
            show()
        }
    }

    fun onDismissFloatWindow() {
        floatView?.dismiss()
    }

    fun genInstance(channelId: String): String? {
        val fragment: String? = when (channelId) {
            PrePageBitmapFragment.KEY_ID -> PrePageBitmapFragment.instance().javaClass.name
            else -> null
        }
        return fragment
    }

    override fun setLandSpaceChange(activity: Activity, isShow: Boolean) {
        LogHelper.get().i("FeedbackPlayerImpl", "showLandSpaceLogoFloatView,isShow=$isShow")
        if (isShow) {
            EasyFloat.updateFloat(FeedbackLogoFloat.TAG, 4000, 800)
            EasyFloat.dragEnable(true, FeedbackLogoFloat.TAG)
        } else {
            EasyFloat.updateFloat(FeedbackLogoFloat.TAG, 1500, 1000)
        }
    }
}