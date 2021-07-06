/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.walker.demo.a2b

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.walker.common.BaseApplication
import com.walker.core.log.LogHelper
import kotlinx.coroutines.coroutineScope
import java.io.File

class AppCopyWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(APP_INSTALL_CONFIG).use { inputStream ->
                val gsonConfig = String(inputStream.readBytes())
                LogHelper.get().i(TAG, "data=$gsonConfig")
            }
            if (File(appLoadPath).exists()) {
                Result.success()
            } else {
                val file = File(appLoadPath)
                if (!file.exists()) {
                    file.parent?.apply {
                        File(this).mkdirs()
                    }
                    file.createNewFile()
                }
                applicationContext.assets.open(APP_FILE_NAME).use { inputStream ->
                    File(appLoadPath).writeBytes(inputStream.readBytes())
                    LogHelper.get().d(TAG, "Copy apk file from assets")
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            LogHelper.get().e(TAG, ex.message)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "AppCopyWorker"
        private const val APP_INSTALL_CONFIG = "install/config.txt"
        private const val APP_FILE_NAME = "install/optimize-crash-debug.apk"
        val appLoadPath =
            "${BaseApplication.context?.externalCacheDir?.path}/install/optimize-crash-debug.apk"
    }
}
