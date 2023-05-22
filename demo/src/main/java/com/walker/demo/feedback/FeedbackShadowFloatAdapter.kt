package com.walker.demo.feedback

import android.view.MotionEvent
import android.view.View
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.demo.R

class FeedbackShadowFloatAdapter : FloatViewAdapter<String>() {

    companion object {
        const val DATA_FLOAT_OPEN = "1"
    }

    private var datas = DATA_FLOAT_OPEN

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_feedback_shadow

    override fun bindView(view: View, data: String) {
    }

    override fun touchEvent(view: View, event: MotionEvent) {
    }

    override fun dragIng(view: View, event: MotionEvent) {
    }

    override fun dragEnd(view: View) {
    }
}