package com.walker.demo.feedback

import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.utils.DisplayUtils
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.core.log.LogHelper
import com.walker.demo.R
import kotlin.math.abs


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
        if (datas == DATA_FLOAT_OPEN) {
            setFloatGroupShow(view, true)
        } else if (datas == DATA_FLOAT_CLOSE) {
            setFloatGroupShow(view, false)
        }
        view.findViewById<View>(R.id.groupFloatOpen).setOnClickListener {
            //唤起业务处理悬浮框
            showTransactFloatView(view)
            //隐藏logo悬浮窗
            EasyFloat.hide(FeedbackLogoFloat.TAG)
        }
        view.findViewById<View>(R.id.groupFloatHide).setOnClickListener {
            setFloatGroupShow(view, true)
        }
    }

    private fun setFloatGroupShow(view: View, isShow: Boolean) {
        view?.apply {
            if (isShow) {
                findViewById<View>(R.id.groupFloatOpen)?.visibility = View.VISIBLE
                findViewById<View>(R.id.groupFloatHide)?.visibility = View.GONE
            } else {
                findViewById<View>(R.id.groupFloatOpen)?.visibility = View.GONE
                findViewById<View>(R.id.groupFloatHide)?.visibility = View.VISIBLE
            }
            EasyFloat.dragEnable(isShow, FeedbackLogoFloat.TAG)
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

    var currentX = 0f
    var currentY = 0f

    override fun dragIng(view: View, event: MotionEvent) {
        LogHelper.get().i(FeedbackLogoFloat.TAG, "dragIng (rawX=${event.rawX},rawY=${event.rawY})")
        dragXList.add(event.rawX)
        //非横向滑动则return
        if (abs(event.rawX - currentX) < abs(event.rawY - currentY)) {
            currentX = event.rawX
            currentY = event.rawY
            return
        }
        currentX = event.rawX
        currentY = event.rawY

        if (event.rawX > halfStateMinX) {
            //符合条件，显示半球
            if (!isHalfLogo && dragXList.size > 3) {
                val x3 = dragXList[dragXList.size - 1]
                val x2 = dragXList[dragXList.size - 2]
                val x1 = dragXList[dragXList.size - 3]
                if (x1 >= halfStateMinX && x3 > x2 && x2 > x1) {
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
            execLogoAnimator(view, halfX, 0f)
            setFloatGroupShow(view, false)
            isHalfLogo = false
        }
    }

    private fun execLogoAnimator(view: View, fromX: Float, toX: Float, duration: Long = 500) {
        val outAnim: ObjectAnimator = ObjectAnimator.ofFloat(view, "x", fromX, toX)
        outAnim.duration = duration
        outAnim.start()
    }
}