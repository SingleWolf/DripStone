package com.walker.demo.floatview

import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.walker.common.router.IWebviewRouter
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.core.router.RouterLoader
import com.walker.demo.R

class MeetingFloatViewAdapter : FloatViewAdapter<MeetingComeBean>() {

    private var datas: MeetingComeBean =
        MeetingComeBean(R.drawable.luoli, "大威天龙", "邀请你加入及时会议", "https://www.baidu.com")

    fun setData(data: MeetingComeBean) {
        datas = data
    }

    override fun getData(): MeetingComeBean {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_meeting_come

    override fun bindView(view: View, data: MeetingComeBean) {
        view.findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            getCallback()?.callback(1)
        }
        view.findViewById<ImageView>(R.id.ivJoin).setOnClickListener {
            getCallback()?.callback(2)
            val webviewRouter = RouterLoader.load(IWebviewRouter::class.java)
            webviewRouter?.startActivity(
                it.context,
                data.title,
                data.url
            )
        }
        view.findViewById<ImageView>(R.id.ivHead).setImageResource(data.headResId)
        view.findViewById<TextView>(R.id.tvTitle).text = data.title
        view.findViewById<TextView>(R.id.tvDescription).text = data.desc
    }

    override fun touchEvent(view: View, event: MotionEvent) {

    }

    override fun dragIng(view: View, event: MotionEvent) {
    }

    override fun dragEnd(view: View) {
    }

}