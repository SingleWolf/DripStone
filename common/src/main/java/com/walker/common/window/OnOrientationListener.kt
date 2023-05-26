package com.walker.common.window

import android.app.Activity

interface OnOrientationListener {
    fun setLandSpaceChange(activity: Activity, isShow: Boolean)
}