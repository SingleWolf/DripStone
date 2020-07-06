package com.walker.ui.group.floatlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.walker.core.log.LogHelper
import kotlin.math.max

/**
 *@Author Walker
 *
 *@Date   2020-07-06 14:10
 *
 *@Summary 手写流布局
 */
class FloatLayout : ViewGroup {

    companion object {
        const val TAG = "FloatLayout"
        const val horizontalSpace = 50
        const val verticalSpace = 60
    }

    private var childViewList = mutableListOf<MutableList<View>>()
    private var lineHeightList = mutableListOf<Int>()
    private var lineViews = mutableListOf<View>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        clearMeasureParams()

        var lineUsedWidth = 0
        var lineUsedHeight = 0

        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        val selfHeight = MeasureSpec.getSize(heightMeasureSpec)
        var selfNeedWidth = 0
        var selfNeedHeight = 0

        //先测量子view
        for (i in 0 until childCount) {
            getChildAt(i)?.let {
                //将layoutParams转变为measureSpec
                val childLayoutParams = it.layoutParams
                val childWidthMeasureSpec = getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingLeft + paddingRight,
                    childLayoutParams.width
                )
                val childHeightMeasureSpec = getChildMeasureSpec(
                    heightMeasureSpec,
                    paddingTop + paddingBottom,
                    childLayoutParams.height
                )
                it.measure(childWidthMeasureSpec, childHeightMeasureSpec)

                //判断是否需要换行
                if (selfWidth < it.measuredWidth + lineUsedWidth + horizontalSpace) {
                    childViewList.add(lineViews)
                    lineHeightList.add(lineUsedHeight)

                    selfNeedWidth = max(selfNeedWidth, lineUsedWidth + horizontalSpace)
                    selfNeedHeight += lineUsedHeight + verticalSpace

                    lineViews= mutableListOf()

                    lineUsedWidth = 0
                    lineUsedHeight = 0
                }

                //将view收入该行集合中
                lineViews.add(it)

                //更新每一行已有宽高
                lineUsedWidth += it.measuredWidth + horizontalSpace
                lineUsedHeight = max(lineUsedHeight, it.measuredHeight)

                if (i == childCount - 1) {
                    childViewList.add(lineViews)
                    lineHeightList.add(lineUsedHeight)

                    selfNeedWidth = max(selfNeedWidth, lineUsedWidth + horizontalSpace)
                    selfNeedHeight += lineUsedHeight + verticalSpace
                }
            }
        }

        //测量自己
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val selfRealWidth = if (widthMode == MeasureSpec.EXACTLY) {
            selfWidth
        } else {
            selfNeedWidth
        }
        val selfRealHeight = if (heightMode == MeasureSpec.EXACTLY) {
            selfHeight
        } else {
            selfNeedHeight
        }
        LogHelper.get().d(TAG, "onMeasure:($selfRealWidth,$selfRealHeight)")
        setMeasuredDimension(selfRealWidth, selfRealHeight)
    }

    private fun clearMeasureParams() {
        childViewList.clear()
        lineViews.clear()
        lineHeightList.clear()

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var curL = paddingLeft
        var curT = paddingTop
        for (index in childViewList.indices) {
            val lineHeight = lineHeightList[index]
            childViewList[index]?.forEach {
                val l = curL
                val t = curT
                var r = l + it.measuredWidth
                var b = t + it.measuredHeight
                it.layout(l, t, r, b)
                curL = r + horizontalSpace
            }
            curL = paddingLeft
            curT += lineHeight + verticalSpace

        }
    }
}