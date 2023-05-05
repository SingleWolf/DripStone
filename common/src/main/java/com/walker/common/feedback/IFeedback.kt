package com.walker.common.feedback

import android.app.Activity
import android.graphics.Bitmap

interface IFeedback {

    fun showPop(activity: Activity)

    fun dismissPop()

    fun getPrePageImage(activity: Activity): Bitmap?
}