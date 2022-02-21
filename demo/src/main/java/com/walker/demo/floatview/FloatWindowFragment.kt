package com.walker.demo.floatview

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.core.base.mvc.BaseFragment
import com.walker.demo.R

/**
 * Author  : walker
 * Date    : 2022/2/18  9:51 上午
 * Email   : feitianwumu@163.com
 * Summary : 悬浮框
 */
class FloatWindowFragment : BaseFragment(), View.OnClickListener {

    companion object {
        const val TAG = "FloatWindowFragment"
        const val KEY_ID = "key_float_window"
        fun instance(): Fragment {
            return FloatWindowFragment()
        }
    }

    private lateinit var tvShowFloat: TextView
    private lateinit var tvUpdateFloat: TextView
    private lateinit var tvDismissFloat: TextView
    private val floatView by lazy { MeetingFloatView(requireContext()) }
    private val floatViewAdapter by lazy { MeetingFloatViewAdapter() }

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        tvShowFloat = baseView.findViewById(R.id.tvShowFloat)
        tvUpdateFloat = baseView.findViewById(R.id.tvUpdateFloat)
        tvDismissFloat = baseView.findViewById(R.id.tvDismissFloat)

        tvShowFloat.setOnClickListener(this)
        tvUpdateFloat.setOnClickListener(this)
        tvDismissFloat.setOnClickListener(this)
    }

    override fun getLayoutId() = R.layout.fragment_demo_float_window

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvShowFloat -> {
                showFloatWindow()
            }
            R.id.tvUpdateFloat -> {
                updateFloatWindow()
            }
            R.id.tvDismissFloat -> {
                onDismissFloatWindow()
            }
        }
    }

    fun showFloatWindow() {
        PermissionX.init(this)
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
                        startFloatWindow()
                    }
                }
            )
    }

    fun startFloatWindow() {
        floatViewAdapter.setData(
            MeetingComeBean(R.drawable.luoli, "大威天龙", "邀请你加入及时会议", "https://www.baidu.com")
        )
        floatViewAdapter.setCallback<Int>(object : FloatViewAdapter.OnCallback {
            override fun <Int> callback(value: Int) {
                floatView.dismiss()
            }

        })
        floatView.setAdapter(floatViewAdapter)
        floatView.show()
    }

    fun onDismissFloatWindow() {
        floatView.dismiss()
    }


    fun updateFloatWindow() {
        if (floatView.isShow()) {
            floatViewAdapter.setData(
                MeetingComeBean(R.drawable.gaoguai, "张三丰", "邀请你加入及时会议", "https://www.sina.com.cn")
            )
            floatViewAdapter.notifyDataChanged()
        } else {
            floatViewAdapter.setData(
                MeetingComeBean(R.drawable.gaoguai, "张三丰", "邀请你加入及时会议", "https://www.sina.com.cn")
            )
            floatViewAdapter.setCallback<Int>(object : FloatViewAdapter.OnCallback {
                override fun <Int> callback(value: Int) {
                    floatView.dismiss()
                }

            })
            floatView.setAdapter(floatViewAdapter)
            floatView.show()
        }
    }
}