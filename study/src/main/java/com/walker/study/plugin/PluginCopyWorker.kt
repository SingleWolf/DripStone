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

package com.walker.study.plugin

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.walker.common.BaseApplication
import com.walker.core.log.LogHelper
import com.walker.study.PLUGIN_FILE_NAME
import kotlinx.coroutines.coroutineScope
import java.io.File

class PluginCopyWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            if (File(BaseApplication.pluginLoadPath).exists()) {
                Result.success()
            } else {
                val file = File(BaseApplication.pluginLoadPath)
                if (!file.exists()) {
                    file.parent?.apply {
                        File(this).mkdirs()
                    }
                    file.createNewFile()
                }

                applicationContext.assets.open(PLUGIN_FILE_NAME).use { inputStream ->
                    File(BaseApplication.pluginLoadPath).writeBytes(inputStream.readBytes())
                    LogHelper.get().d(TAG, "Copy plugin file from assets")
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            LogHelper.get().e(TAG, ex.message)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "PluginCopyWorker"
    }
}
