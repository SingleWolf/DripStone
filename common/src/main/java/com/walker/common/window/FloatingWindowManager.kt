package com.walker.common.window

import android.app.Activity
import android.app.Application
import android.graphics.Bitmap
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.walker.common.util.DrawableHelper
import com.walker.core.util.DisplayUtils

object FloatingWindowManager {

    private val lifecycle by lazy {
        FloatingActivityLifecycle()
    }

    @JvmStatic
    fun init(context: Application) {
        context.registerActivityLifecycleCallbacks(lifecycle)
    }


    /**
     * 判断该activity是否有弹框（可能有误判）
     */
    fun hasFloatingWindowByView(activity: Activity): Boolean {
        return getFloatWindowViewByView(activity).isNotEmpty()
    }


    fun getFloatWindowViewByView(activity: Activity): List<View> {
        val targetDecorView = activity.window.decorView
        val acDecorViews = lifecycle.getActivities().map { it.window.decorView }.toList()
        val mView = Window.getViews().map { it }.toList()
        val targetIndex = mView.first { it == targetDecorView }
        val index = mView.indexOf(targetIndex)
        val floatView = arrayListOf<View>()
        for (i in index + 1 until mView.size) {
            if (acDecorViews.contains(mView[i])) {
                break
            }
            floatView.add(mView[i])
        }
        return floatView
    }

    /**
     * 判断该activity是否有弹框（比较准确）
     */
    fun hasFloatWindowByToken(activity: Activity): Boolean {
        // 获取目标 Activity 的 decorView
        val targetDecorView = activity.window.decorView
        // 获取目标 Activity 的 子窗口的 token
        val targetSubToken = targetDecorView.windowToken


        //  拿到 mView 集合，找到目标 Activity 所在的 index 位置
        val mView = Window.getViews().map { it }.toList()
        val targetIndex = mView.indexOfFirst { it == targetDecorView }

        // 获取 mParams 集合
        val mParams = Window.getParams()
        // 根据目标 index 从 mParams 集合中找到目标 token
        val targetToken = mParams[targetIndex].token


        // 遍历判断时，目标 Activity 自己不能包括,所以 size 需要大于 1
        return mParams
            .map { it.token }
            .filter { it == targetSubToken || it == null || it == targetToken }
            .size > 1
    }


    fun getFloatWindowViewByToken(activity: Activity): List<View> {
        // 获取目标 Activity 的 decorView
        val targetDecorView = activity.window.decorView
        // 获取目标 Activity 的 子窗口的 token
        val targetSubToken = targetDecorView.windowToken


        //  拿到 mView 集合，找到目标 Activity 所在的 index 位置
        val mView = Window.getViews().map { it }.toList()
        val targetIndex = mView.indexOfFirst { it == targetDecorView }

        // 获取 mParams 集合
        val mParams = Window.getParams()
        // 根据目标 index 从 mParams 集合中找到目标 token
        val targetToken = mParams[targetIndex].token


        val floatView = arrayListOf<View>()

        mParams.forEachIndexed { index, params ->
            val token = params.token
            // Activity 自身不参与
            if (index != targetIndex) {
                if (token == targetSubToken || token == null || token == targetToken) {
                    // 根据 index 拿到 mView 中的 View
                    floatView.add(mView[index])
                }
            }
        }
        return floatView
    }

    fun listWindowViewByToken(activity: Activity): List<View> {
        // 获取目标 Activity 的 decorView
        val targetDecorView = activity.window.decorView
        // 获取目标 Activity 的 子窗口的 token
        val targetSubToken = targetDecorView.windowToken


        //  拿到 mView 集合，找到目标 Activity 所在的 index 位置
        val mView = Window.getViews().map { it }.toList()
        val targetIndex = mView.indexOfFirst { it == targetDecorView }

        // 获取 mParams 集合
        val mParams = Window.getParams()
        // 根据目标 index 从 mParams 集合中找到目标 token
        val targetToken = mParams[targetIndex].token


        val floatView = arrayListOf<View>()

        mParams.forEachIndexed { index, params ->
            val token = params.token
            if (token == targetSubToken || token == null || token == targetToken) {
                // 根据 index 拿到 mView 中的 View
                floatView.add(mView[index])
            }
        }
        return floatView
    }


    /**
     * 获取前一个页面的截图
     */
    fun genBitmapFromPreActivityView(activity: Activity): Bitmap? {
//        val targetDecorView = activity.window.decorView
//        val mView = Window.getViews().map { it }.toList()
//        val targetIndex = mView.first { it == targetDecorView }
//        val index = mView.indexOf(targetIndex)
//        val preIndex = index - 1
//        if (preIndex >= 0 && preIndex < mView.size) {
//           mView[preIndex]?.apply {
//               return DrawableHelper.createBitmapFromView(this)
//           }
//        }

        val index = lifecycle.getActivities().indexOfFirst { it == activity }
        val preIndex = index - 1
        if (preIndex >= 0 && preIndex < lifecycle.getActivities().size) {
            lifecycle.getActivities()[preIndex]?.apply {
                return genBitmapFromViews(this, listWindowViewByToken(this))
            }
        }
        return null
    }

    private fun genBitmapFromViews(activity: Activity, viewList: List<View>): Bitmap? {
        val contentLayout = FrameLayout(activity)
        val parentLayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        contentLayout.layoutParams = parentLayoutParams

        val subLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        subLayoutParams.gravity = Gravity.CENTER

        viewList.forEach {
            DrawableHelper.createBitmapFromView(it)?.apply {
                val imageView = ImageView(activity)
                imageView.layoutParams = subLayoutParams
                imageView.setImageBitmap(this)
                contentLayout.addView(imageView)
            }
        }

        //对于未展示在界面上的view进行测量、布局、获取真实大小
        val screenWH = DisplayUtils.getDisplaySize(activity)
        contentLayout.measure(
            View.MeasureSpec.makeMeasureSpec(
                screenWH.x,
                View.MeasureSpec.EXACTLY
            ), View.MeasureSpec.makeMeasureSpec(screenWH.y, View.MeasureSpec.EXACTLY)
        )
        contentLayout.layout(0, 0, contentLayout.measuredWidth, contentLayout.measuredHeight)

        return DrawableHelper.createBitmapFromView(contentLayout)
    }
}