package com.walker.demo.feedback

import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.demo.R

class FeedbackShadowFloatAdapter : FloatViewAdapter<String>() {

    companion object {
        const val DATA_FLOAT_RIGHT = "1"
        const val DATA_FLOAT_LEFT = "2"
    }

    private var datas = DATA_FLOAT_RIGHT

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_feedback_shadow

    override fun bindView(view: View, data: String) {
        val groupFloatShadow = view.findViewById<ImageView>(R.id.groupFloatShadow)
        if (datas == DATA_FLOAT_RIGHT) {
            groupFloatShadow.setImageResource(R.drawable.ic_demo_feedback_half_shadow)
        } else {
            groupFloatShadow.setImageResource(R.drawable.ic_demo_feedback_half_shadow_left)
        }
    }

    override fun touchEvent(view: View, event: MotionEvent) {
    }

    override fun dragIng(view: View, event: MotionEvent) {
    }

    override fun dragEnd(view: View) {
    }
}