package com.walker.common.view.watermark

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import com.walker.core.log.LogHelper

object WaterMarkHelper {

    const val TAG = "WaterMarkHelper"

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                transactWaterMark(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })
    }

    private fun transactWaterMark(activity: Activity) {
        activity?.javaClass.declaredMethods.forEach {
            it.getAnnotation(WaterMarkTag::class.java)?.apply {
                LogHelper.get().i(TAG, "发现水印标签：${this.markText}")
                addWaterMark(activity, this)
                return
            }
        }
    }

    private fun addWaterMark(activity: Activity, tag: WaterMarkTag) {
        if (TextUtils.isEmpty(tag.markText)) {
            return
        }
        val contentLayout =
            activity.window.decorView.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        contentLayout?.post {
            contentLayout?.also {
                View(activity).apply {
                    ViewGroup.MarginLayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ).also {
                        it.setMargins(
                            tag.marginLeft,
                            tag.marginTop,
                            tag.marginRight,
                            tag.marginBottom
                        )
                        this.layoutParams = it
                    }
                    background = WaterMarkView(activity, tag.markText, tag.angle, tag.textSize)
                    it.addView(this)
                }
            }
        }
    }
}