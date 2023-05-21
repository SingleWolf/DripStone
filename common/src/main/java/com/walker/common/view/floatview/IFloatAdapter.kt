package com.walker.common.view.floatview

import android.view.MotionEvent
import android.view.View

interface IFloatAdapter {
    fun getLayoutId():Int
    fun attachView(view: View)
    fun notifyDataChanged()

    fun touchEvent(view: View, event: MotionEvent)

    fun dragIng(view: View, event: MotionEvent)

    fun dragEnd(view: View)
}