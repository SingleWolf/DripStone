package com.walker.common.window

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import kotlin.collections.ArrayList

internal class FloatingActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private val mActivities = ArrayList<Activity>()

    private var orientationListener: OnOrientationListener? = null

    private var currentLandSpaceActivityHash = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mActivities.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        val orientation = activity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentLandSpaceActivityHash = activity.hashCode()
            orientationListener?.setLandSpaceChange(activity, true)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (currentLandSpaceActivityHash == activity.hashCode()) {
            currentLandSpaceActivityHash = 0
            orientationListener?.setLandSpaceChange(activity, false)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        mActivities.remove(activity)
    }

    fun getActivities(): List<Activity> {
        return mActivities.toList()
    }

    open fun setOnOrientationListener(listener: OnOrientationListener) {
        orientationListener = listener
    }
}