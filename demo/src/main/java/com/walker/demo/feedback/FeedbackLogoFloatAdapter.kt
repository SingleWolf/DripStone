package com.walker.demo.feedback

import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.utils.DisplayUtils
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.core.log.LogHelper
import com.walker.demo.R


class FeedbackLogoFloatAdapter : FloatViewAdapter<String>() {

    companion object {
        const val DATA_FLOAT_OPEN = "1"
        const val DATA_FLOAT_CLOSE = "2"

        const val CODE_CLICK_BUSINESS = 1
        const val CODE_CLICK_REPORT = 2

    }

    private val transactFloatAdapter by lazy { FeedbackTransactFloatAdapter() }
    private var transactFloatView: FeedbackTransactFloat? = null

    private val shadowFloatAdapter by lazy { FeedbackShadowFloatAdapter() }
    private var shadowFloatView: FeedbackShadowFloat? = null

    private var datas = DATA_FLOAT_CLOSE

    /**
     * 记录当前打开、隐藏状态
     */
    private var currentIsOpen = true

    /**
     * 拖动过程中的X触点集合
     */
    private var dragXList = mutableListOf<Float>()

    /**
     * 标记是否为半球隐藏提示状态
     */
    private var isHalfLogo = false

    /**
     * 触发半球隐藏状态的最小值
     */
    private var halfStateMinX = 950f

    /**
     * 隐藏logo的X坐标点
     */
    private val hideX = 150f

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_feedback_logo

    override fun bindView(view: View, data: String) {
        //超过当前logo宽度的1/5
        halfStateMinX = (DisplayUtils.getScreenWidth(view.context) - (view.width / 5 * 4)).toFloat()
        //当前logo向右平移2/3
        val hideX = (view.width / 3 * 2).toFloat()

        if (datas == DATA_FLOAT_OPEN) {
            if (!currentIsOpen) {
                execLogoAnimator(view, hideX, 0f)
            }
            currentIsOpen = true
        } else if (datas == DATA_FLOAT_CLOSE) {
            if (currentIsOpen) {
                execLogoAnimator(view, 0f, hideX, 0)
            }
            currentIsOpen = false
        }
        view.findViewById<View>(R.id.groupFloatOpen).setOnClickListener {
            if (currentIsOpen) {
                //唤起业务处理悬浮框
                showTransactFloatView(view)
                //隐藏logo悬浮窗
                EasyFloat.hide(FeedbackLogoFloat.TAG)
            } else {
                execLogoAnimator(view, hideX, 0f, 300)
                currentIsOpen = true
            }
        }
    }

    private fun showTransactFloatView(view: View) {
        val context = view.context
        //获取图标悬浮框位置
        var locLogo = intArrayOf(0, 0)
        var transactX = 0
        val screenH = DisplayUtils.getScreenHeight(context)
        val locY = screenH - DisplayUtils.dp2px(context, 340f)
        var transactY = locY
        var transactMinY = DisplayUtils.dp2px(context, 220f)
        view.getLocationOnScreen(locLogo)
        LogHelper.get().i(
            FeedbackLogoFloat.TAG,
            "showTransactFloatView locLogo=(x=${locLogo[0]},y=${locLogo[1]})"
        )
        //业务框的高度比logo框高140dp，故减去
        if (locLogo[1] > transactMinY) {
            transactY = locLogo[1] - DisplayUtils.dp2px(context, 168f)
        }
        if (transactFloatView == null) {
            transactFloatView = FeedbackTransactFloat(context)
        }
        transactFloatAdapter.setCallback<Int>(object : FloatViewAdapter.OnCallback {
            override fun <Int> callback(value: Int) {
                getCallback()?.callback(value)
            }

        })
        transactFloatView?.apply {
            setAdapter(transactFloatAdapter)
            showLocation(transactX, transactY)
        }
    }

    private fun showShadowFloatView(view: View) {
        val context = view.context
        var shadowX = 0
        var shadowY = 1200
        //获取图标悬浮框位置
        var locLogo = intArrayOf(0, 0)
        view.getLocationOnScreen(locLogo)
        LogHelper.get().i(
            FeedbackLogoFloat.TAG,
            "showShadowFloatView locLogo=(x=${locLogo[0]},y=${locLogo[1]})"
        )
        if (locLogo[1] > 0) {
            shadowY = locLogo[1] + view.height / 2 - DisplayUtils.dp2px(context, 158f)
        }
        if (shadowFloatView == null) {
            shadowFloatView = FeedbackShadowFloat(context)
        }

        shadowFloatView?.apply {
            setAdapter(shadowFloatAdapter)
            showLocation(shadowX, shadowY)
        }
    }

    private fun dismissShadowFloatView() {
        shadowFloatView?.dismiss()
    }

    override fun touchEvent(view: View, event: MotionEvent) {
    }

    override fun dragIng(view: View, event: MotionEvent) {
        LogHelper.get().i(FeedbackLogoFloat.TAG, "dragIng (rawX=${event.rawX},rawY=${event.rawY})")
        dragXList.add(event.rawX)
        if (event.rawX > halfStateMinX) {
            //符合条件，显示半球
            if (!isHalfLogo && dragXList.size > 5) {
                val x5 = dragXList[dragXList.size - 1]
                val x4 = dragXList[dragXList.size - 2]
                val x3 = dragXList[dragXList.size - 3]
                val x2 = dragXList[dragXList.size - 4]
                val x1 = dragXList[dragXList.size - 5]
                if (x5 > x4 && x4 > x3 && x3 > x2 && x2 > x1) {
                    showShadowFloatView(view)
                    val endX = (view.width / 2).toFloat()
                    execLogoAnimator(view, 0f, endX)
                    isHalfLogo = true
                }
            }
        } else {
            if (isHalfLogo) {
                //半球状态撤销
                val startX = (view.width / 2).toFloat()
                execLogoAnimator(view, startX, 0f)
                dismissShadowFloatView()
                isHalfLogo = false
            }
        }
    }

    override fun dragEnd(view: View) {
        dragXList.clear()
        if (isHalfLogo) {
            dismissShadowFloatView()
            val halfX = (view.width / 2).toFloat()
            execLogoAnimator(view, halfX, hideX)
            isHalfLogo = false
            currentIsOpen = false
        }
    }

    private fun execLogoAnimator(view: View, fromX: Float, toX: Float, duration: Long = 500) {
        val outAnim: ObjectAnimator = ObjectAnimator.ofFloat(view, "x", fromX, toX)
        outAnim.duration = duration
        outAnim.start()
    }
}