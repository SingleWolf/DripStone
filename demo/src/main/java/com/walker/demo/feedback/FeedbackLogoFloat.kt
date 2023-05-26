package com.walker.demo.feedback

import android.content.Context
import android.view.Gravity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.utils.DisplayUtils
import com.walker.common.view.floatview.BaseFloatView
import com.walker.core.log.LogHelper

class FeedbackLogoFloat(var context: Context) : BaseFloatView(context) {

    companion object {
        const val TAG = "FeedbackLogoFloat"
    }

    override fun getTag() = TAG

    override fun show() {
        LogHelper.get().i(TAG, "screenWith=${DisplayUtils.getScreenWidth(context)}")
        if (isShow()) {
            floatAdapter?.notifyDataChanged()
        } else {
            floatAdapter?.apply {
                val screenW = DisplayUtils.getScreenWidth(context)
                val screenH = DisplayUtils.getScreenHeight(context)
                val dragMinY = DisplayUtils.dp2px(context, 200f)
                val dragMaxX = screenW
                val dragMaxY = screenH - DisplayUtils.dp2px(context, 75f)
                val locY = screenH - DisplayUtils.dp2px(context, 300f)
                EasyFloat.with(context.applicationContext)
                    .setShowPattern(ShowPattern.FOREGROUND)
                    .setSidePattern(SidePattern.RESULT_RIGHT)
                    .setAnimator(null)
                    .setDragEnable(true)
                    .setBorder(0,dragMinY,dragMaxX,dragMaxY)
                    .setMatchParent(widthMatch = false, heightMatch = false)
                    .setGravity(Gravity.RIGHT, 0, locY)
                    .setLayoutChangedGravity(Gravity.END)
                    .setLayout(getLayoutId()) { it ->
                        attachView(it)
                    }
                    .setTag(getTag())
                    .registerCallback {
                        createResult { isCreated, msg, view ->
                        }
                        show { }
                        hide { }
                        dismiss { }
                        touchEvent { view, motionEvent ->
                            floatAdapter?.touchEvent(view, motionEvent)
                        }
                        drag { view, motionEvent ->
                            floatAdapter?.dragIng(view, motionEvent)
                        }
                        dragEnd {
                            floatAdapter?.dragEnd(it)
                        }
                    }
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