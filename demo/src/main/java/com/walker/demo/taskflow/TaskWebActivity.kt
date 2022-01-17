package com.walker.demo.taskflow

import android.content.Context
import android.content.Intent
import com.walker.webview.WebviewActivity
import com.walker.webview.utils.WebConstants

class TaskWebActivity : WebviewActivity() {

    companion object {
        fun start(context: Context, url: String, title: String = "") {
            var intent = Intent(context, TaskWebActivity::class.java)
            intent.putExtra(WebConstants.INTENT_TAG_TITLE, title)
            intent.putExtra(WebConstants.INTENT_TAG_URL, url)
            if (context !is android.app.Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TaskModel.instance.taskState.postValue(1)
    }
}