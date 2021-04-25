package com.walker.study.skin

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.walker.common.BaseApplication

object SkinWorker {
    fun transactSkin() {
        val request = OneTimeWorkRequest.Builder(SkinCopyWorker::class.java).build()
        WorkManager.getInstance(BaseApplication.context!!).enqueue(request)
    }
}