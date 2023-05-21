package com.walker.demo.feedback

import android.view.MotionEvent
import android.view.View
import com.lzf.easyfloat.EasyFloat
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.demo.R

class FeedbackTransactFloatAdapter : FloatViewAdapter<String>() {

    companion object {
        const val DATA_FLOAT_OPEN = "1"
        const val DATA_FLOAT_CLOSE = "2"

        const val CODE_CLICK_BUSINESS = 1
        const val CODE_CLICK_REPORT = 2

    }

    private var datas = DATA_FLOAT_OPEN

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_feedback_transact

    override fun bindView(view: View, data: String) {
        val flexibleMenu = view.findViewById<FlexibleMenuView>(R.id.flexibleMenu)
        flexibleMenu.setOnMenuItemClickListener { view, pos ->
            when (view.tag) {
                "feedback_report" -> {
                    getCallback()?.callback(CODE_CLICK_REPORT)
                }
                "feedback_business" -> {
                    getCallback()?.callback(CODE_CLICK_BUSINESS)
                }
                else -> {}
            }
        }
        flexibleMenu.setOnMenuShowStateListener {
            if (!it) {
                //隐藏业务悬浮窗
                EasyFloat.dismiss(FeedbackTransactFloat.TAG)
                //展示logo悬浮框
                EasyFloat.show(FeedbackLogoFloat.TAG)
            }
        }
        flexibleMenu.autoOpen()
    }

    override fun touchEvent(view: View, event: MotionEvent) {
    }

    override fun dragIng(view: View, event: MotionEvent) {
    }

    override fun dragEnd(view: View) {
    }
}