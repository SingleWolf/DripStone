package com.walker.demo.feedback

import android.content.Context
import android.view.Gravity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.utils.DisplayUtils
import com.walker.common.view.floatview.BaseFloatView

class FeedbackFloatView(var context: Context) : BaseFloatView(context) {

    companion object {
        const val TAG = "FeedbackFloatView"
    }

    override fun getTag() = TAG

    override fun show() {
        if (isShow()) {
            floatAdapter?.notifyDataChanged()
        } else {
            floatAdapter?.apply {
                EasyFloat.with(context.applicationContext)
                    .setShowPattern(ShowPattern.FOREGROUND)
                    .setSidePattern(SidePattern.RESULT_RIGHT)
                    .setAnimator(null)
                    //.setImmersionStatusBar(true)
                    .setDragEnable(true)
                    .setMatchParent(widthMatch = false, heightMatch = false)
                    .setGravity(Gravity.RIGHT, 0, 1600)
                    .setLayoutChangedGravity(Gravity.END)
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