package com.walker.demo.feedback

import android.content.Context
import android.view.Gravity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.utils.DisplayUtils
import com.walker.common.view.floatview.BaseFloatView
import com.walker.core.log.LogHelper

class FeedbackShadowFloat(var context: Context) : BaseFloatView(context) {

    companion object {
        const val TAG = "FeedbackShadowFloat"
    }

    override fun getTag() = TAG

    override fun show() {
        if (isShow()) {
            floatAdapter?.notifyDataChanged()
        } else {
            floatAdapter?.apply {
                val screenH = DisplayUtils.getScreenHeight(context)
                val locY = screenH - DisplayUtils.dp2px(context, 290f)
                EasyFloat.with(context.applicationContext)
                    .setShowPattern(ShowPattern.FOREGROUND)
                    .setSidePattern(SidePattern.RESULT_RIGHT)
                    .setAnimator(null)
                    .setDragEnable(false)
                    .setMatchParent(widthMatch = false, heightMatch = false)
                    .setGravity(Gravity.RIGHT, 0, locY)
                    .setLayoutChangedGravity(Gravity.END)
                    .setLayout(getLayoutId()) { it ->
                        attachView(it)
                    }
                    .setTag(getTag())
                    .show()
            }
        }
    }

    override fun showLocation(x: Int, y: Int) {
        if (isShow()) {
            EasyFloat.updateFloat(TAG, x, y)
            floatAdapter?.notifyDataChanged()
        } else {
            floatAdapter?.apply {
                EasyFloat.with(context.applicationContext)
                    .setShowPattern(ShowPattern.FOREGROUND)
                    .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                    .setAnimator(null)
                    .setDragEnable(false)
                    .setMatchParent(widthMatch = false, heightMatch = false)
                    .setLocation(x, y)
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