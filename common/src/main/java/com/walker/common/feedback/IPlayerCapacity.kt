package com.walker.common.feedback

import android.app.Activity
import android.view.MotionEvent

interface IPlayerCapacity {
    fun showPop(activity: Activity)

    fun dismissPop()

    fun transact()

    fun getCurrentFlag(): String?

    fun dispatchTouchEventFromScreen(event: MotionEvent)
}