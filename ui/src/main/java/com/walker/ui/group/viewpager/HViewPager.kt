package com.walker.ui.group.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

/**
 *@Author Walker
 *
 *@Date   2020-07-22 17:32
 *
 *@Summary 解决ViewPager高度问题
 */
class HViewPager : ViewPager {

    var realHeight = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        realHeight = 0
        for (i in 0..childCount) {
            val child = getChildAt(i)
            val layoutParam = child.layoutParams
            child.measure(
                widthMeasureSpec,
                ViewGroup.getChildMeasureSpec(heightMeasureSpec, 0, layoutParam.height)
            )
            val h = child.measuredHeight
            if (realHeight < h) {
                realHeight = h
            }
        }
        val realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(realHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, realHeightMeasureSpec)
    }
}