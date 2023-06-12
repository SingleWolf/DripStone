package com.walker.demo.feedback

import android.animation.ObjectAnimator
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
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

    private var datas = DATA_FLOAT_OPEN

    /**
     * 拖动过程中的X触点集合
     */
    private var dragXList = mutableListOf<Float>()

    /**
     * 标记是否为半球隐藏提示状态
     */
    private var hideState: FeedbackHideState = FeedbackHideState.NONE

    /**
     * 左侧触发半球隐藏状态的最小值
     */
    private var halfStateRightMinX = 950f

    /**
     * 右侧触发半球隐藏状态的最大值
     */
    private var halfStateLeftMaX = 100f

    private var isLogoOpen = true

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_feedback_logo

    override fun bindView(view: View, data: String) {
        //超过当前logo宽度的1/5
        halfStateLeftMaX = (view.width / 5 * 4).toFloat()
        halfStateRightMinX = DisplayUtils.getScreenWidth(view.context) - halfStateLeftMaX
        if (datas == DATA_FLOAT_OPEN) {
            isLogoOpen = true
            setFloatGroupShow(view, true)
        } else if (datas == DATA_FLOAT_CLOSE) {
            isLogoOpen = false
            var locLogo = intArrayOf(0, 0)
            view.getLocationOnScreen(locLogo)
            if (locLogo[0] > 500) {
                execLogoAnimator(view, 0f, (view.width / 2).toFloat(), 0)
            } else {
                execLogoAnimator(view, 0f, -(view.width / 2).toFloat(), 0)
            }
            setFloatGroupShow(view, false)
        }
        view.findViewById<View>(R.id.groupFloatOpen).setOnClickListener {
            if (isLogoOpen) {
                //唤起业务处理悬浮框
                showTransactFloatView(view)
                //隐藏logo悬浮窗
                EasyFloat.hide(FeedbackLogoFloat.TAG)
            } else {
                isLogoOpen = true
                setFloatGroupShow(view, true)
                var locLogo = intArrayOf(0, 0)
                view.getLocationOnScreen(locLogo)
                if (locLogo[0] > 500) {
                    execLogoAnimator(view, (view.width / 2).toFloat(), 0f)
                } else {
                    execLogoAnimator(view, -(view.width / 2).toFloat(), 0f)
                }
            }
        }
    }

    private fun setFloatGroupShow(view: View, isShow: Boolean) {
        view?.apply {
            if (isShow) {
                findViewById<ImageView>(R.id.groupFloatOpen)?.setImageResource(R.drawable.ic_demo_feedback)
            } else {
                var locLogo = intArrayOf(0, 0)
                view.getLocationOnScreen(locLogo)
                if (locLogo[0] > 500) {
                    findViewById<ImageView>(R.id.groupFloatOpen)?.setImageResource(R.drawable.ic_demo_feedback_hide)
                } else {
                    findViewById<ImageView>(R.id.groupFloatOpen)?.setImageResource(R.drawable.ic_demo_feedback_hide_left)
                }
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

        transactX = locLogo[0]
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
        if (hideState is FeedbackHideState.RIGHT) {
            shadowX = DisplayUtils.getScreenWidth(context)
            shadowFloatAdapter.setData(FeedbackShadowFloatAdapter.DATA_FLOAT_RIGHT)
        } else if (hideState is FeedbackHideState.LEFT) {
            shadowX = 0
            shadowFloatAdapter.setData(FeedbackShadowFloatAdapter.DATA_FLOAT_LEFT)
        }
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

        if (event.rawX > halfStateRightMinX) {
            //右侧滑动符合条件，显示半球
            if (hideState is FeedbackHideState.NONE && dragXList.size > 3) {
                val x3 = dragXList[dragXList.size - 1]
                val x2 = dragXList[dragXList.size - 2]
                val x1 = dragXList[dragXList.size - 3]
                if (x1 >= halfStateRightMinX && x3 > x2 && x2 > x1) {
                    hideState = FeedbackHideState.RIGHT
                    showShadowFloatView(view)
                    val endX = (view.width / 2).toFloat()
                    execLogoAnimator(view, 0f, endX)
                }
            }
        } else if (event.rawX < halfStateLeftMaX) {
            //左侧滑动符合条件，显示半球
            if (hideState is FeedbackHideState.NONE && dragXList.size > 3) {
                val x3 = dragXList[dragXList.size - 1]
                val x2 = dragXList[dragXList.size - 2]
                val x1 = dragXList[dragXList.size - 3]
                if (x3 < halfStateLeftMaX && x3 < x2 && x2 < x1) {
                    hideState = FeedbackHideState.LEFT
                    showShadowFloatView(view)
                    val endX = (view.width / 2).toFloat()
                    execLogoAnimator(view, 0f, -endX)
                }
            }
        } else {
            if (hideState is FeedbackHideState.RIGHT) {
                //半球状态撤销
                val startX = (view.width / 2).toFloat()
                execLogoAnimator(view, startX, 0f)
                dismissShadowFloatView()
                hideState = FeedbackHideState.NONE
            } else if (hideState is FeedbackHideState.LEFT) {
                //半球状态撤销
                val startX = (view.width / 2).toFloat()
                execLogoAnimator(view, -startX, 0f)
                dismissShadowFloatView()
                hideState = FeedbackHideState.NONE
            }
        }
    }

    override fun dragEnd(view: View) {
        dragXList.clear()
        if (hideState is FeedbackHideState.RIGHT) {
            dismissShadowFloatView()
            isLogoOpen = false
            setFloatGroupShow(view, false)
            hideState = FeedbackHideState.NONE
        } else if (hideState is FeedbackHideState.LEFT) {
            dismissShadowFloatView()
            isLogoOpen = false
            setFloatGroupShow(view, false)
            hideState = FeedbackHideState.NONE
        }
    }

    private fun execLogoAnimator(view: View, fromX: Float, toX: Float, duration: Long = 500) {
        val outAnim: ObjectAnimator = ObjectAnimator.ofFloat(view, "x", fromX, toX)
        outAnim.duration = duration
        outAnim.start()
    }
}