package com.walker.demo.feedback

import android.Manifest
import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.common.activity.ShowActivity
import com.walker.common.feedback.IPlayerCapacity
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.core.util.ToastUtils
import com.walker.demo.window.PrePageBitmapFragment

class FeedbackPlayerImpl : IPlayerCapacity {

    private var floatView: FeedbackFloatView? = null
    private val floatViewAdapter by lazy { FeedbackFloatViewAdapter() }

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

    fun startFloatWindow(activity: Activity) {
        floatViewAdapter.setCallback<Int>(object : FloatViewAdapter.OnCallback {
            override fun <Int> callback(value: Int) {
                //transact()
                if (value == FeedbackFloatViewAdapter.CODE_CLICK_REPORT) {
                    floatView?.apply {
                        ShowActivity.start(
                            context,
                            PrePageBitmapFragment.KEY_ID,
                            "PrePageBitmapFragment",
                            ::genInstance
                        )
                    }
                }else if(value==FeedbackFloatViewAdapter.CODE_CLICK_BUSINESS){
                    ToastUtils.showCenter("业务办理")
                }
            }

        })
        if (floatView == null) {
            floatView = FeedbackFloatView(activity)
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
}