package com.walker.demo.feedback

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.demo.R

class FeedbackFloatViewAdapter : FloatViewAdapter<String>() {

    companion object{
        const val DATA_FLOAT_OPEN="1"
        const val DATA_FLOAT_CLOSE="2"

        const val CODE_CLICK_BUSINESS=1
        const val CODE_CLICK_REPORT=2

    }

    private var datas = DATA_FLOAT_OPEN

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_feedback_logo

    override fun bindView(view: View, data: String) {
        if (datas == DATA_FLOAT_OPEN) {
            view.findViewById<View>(R.id.groupFloatOpen).visibility = View.VISIBLE
            view.findViewById<View>(R.id.groupFloatClose).visibility = View.GONE

            view.findViewById<TextView>(R.id.tvBusinessClick).setOnClickListener {
                getCallback()?.callback(CODE_CLICK_BUSINESS)
            }
            view.findViewById<TextView>(R.id.tvFeedbackClick).setOnClickListener {
                getCallback()?.callback(CODE_CLICK_REPORT)
            }
            view.findViewById<ImageView>(R.id.ivHeadHide).setOnClickListener {
                setData(DATA_FLOAT_CLOSE)
                notifyDataChanged()
            }
        } else {
            view.findViewById<View>(R.id.groupFloatOpen).visibility = View.GONE
            view.findViewById<View>(R.id.groupFloatClose).visibility = View.VISIBLE

            view.findViewById<View>(R.id.groupFloatClose).setOnClickListener {
                setData(DATA_FLOAT_OPEN)
                notifyDataChanged()
            }
        }
    }

}