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

package com.walker.study.skin

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.walker.common.BaseApplication
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.study.PLUGIN_FILE_NAME
import com.walker.study.SKIN_BLACK__FILE_NAME
import com.walker.study.SKIN_BLUE__FILE_NAME
import kotlinx.coroutines.coroutineScope
import java.io.File

class SkinCopyWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            if (File(SkinFragment.SKIN_BLUE_PATH).exists() && File(SkinFragment.SKIN_BLACK_PATH).exists()) {
                ToastUtils.show("加载成功[已存在]")
                Result.success()
            } else {
                val file = File(SkinFragment.SKIN_BLUE_PATH)
                if (!file.exists()) {
                    file.parent?.apply {
                        File(this).mkdirs()
                    }
                    file.createNewFile()
                }

                applicationContext.assets.open(SKIN_BLUE__FILE_NAME).use { inputStream ->
                    File(SkinFragment.SKIN_BLUE_PATH).writeBytes(inputStream.readBytes())
                    LogHelper.get().d(TAG, "Copy skin-blue.apk file from assets")
                }
                applicationContext.assets.open(SKIN_BLACK__FILE_NAME).use { inputStream ->
                    File(SkinFragment.SKIN_BLACK_PATH).writeBytes(inputStream.readBytes())
                    LogHelper.get().d(TAG, "Copy skin-black.apk from assets")
                }
                ToastUtils.show("加载成功")
                Result.success()
            }
        } catch (ex: Exception) {
            LogHelper.get().e(TAG, ex.message)
            ToastUtils.show("加载失败")
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SkinCopyWorker"
    }
}
