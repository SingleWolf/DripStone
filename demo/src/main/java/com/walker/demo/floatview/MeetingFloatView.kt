package com.walker.demo.floatview

import android.content.Context
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.walker.common.view.floatview.BaseFloatView

class MeetingFloatView(var context: Context) : BaseFloatView(context) {
    override fun getTag() = "MeetingComeFloatView"

    override fun show() {
        if (isShow()) {
            floatAdapter?.notifyDataChanged()
        } else {
            floatAdapter?.apply {
                EasyFloat.with(context.applicationContext)
                    .setShowPattern(ShowPattern.FOREGROUND)
                    .setSidePattern(SidePattern.AUTO_SIDE)
                    .setAnimator(null)
                    //.setImmersionStatusBar(true)
                    .setDragEnable(false)
                    .setMatchParent(widthMatch = true, heightMatch = false)
                    .setLocation(0, 200)
                    .setLayout(getLayoutId()) { it ->
                        attachView(it)
                    }
                    .setTag(getTag())
                    .show()
            }
        }
    }

    override fun dismiss() {
        if (isShow()) {
            EasyFloat.dismiss(getTag(), false)
        }
    }

    override fun isShow() = EasyFloat.isShow(getTag())
}