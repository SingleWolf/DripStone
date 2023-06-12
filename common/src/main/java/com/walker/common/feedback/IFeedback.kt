package com.walker.common.feedback

import android.app.Activity
import android.graphics.Bitmap
import android.view.MotionEvent

interface IFeedback {

    fun showPop(activity: Activity)

    fun dismissPop()

    fun getPrePageImage(activity: Activity): Bitmap?

    fun dispatchTouchEventFromScreen(event: MotionEvent)
}