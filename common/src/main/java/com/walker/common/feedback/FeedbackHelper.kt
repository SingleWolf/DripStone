package com.walker.common.feedback

import android.app.Activity
import android.app.Application
import com.walker.common.window.FloatingWindowManager

object FeedbackHelper : IFeedback {

    var playerProxy: IPlayerCapacity? = null

    fun config(context: Application, player: IPlayerCapacity) {
        FloatingWindowManager.init(context)
        playerProxy = player
    }

    override fun showPop(activity: Activity) {
        playerProxy?.showPop(activity)
    }

    override fun dismissPop() {
        playerProxy?.dismissPop()
    }

    override fun getPrePageImage(activity: Activity) =
        FloatingWindowManager.genBitmapFromPreActivityView(activity)
}