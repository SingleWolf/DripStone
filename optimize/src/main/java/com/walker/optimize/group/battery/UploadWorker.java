package com.walker.optimize.group.battery;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.walker.core.log.LogHelper;


public class UploadWorker extends Worker {
    private static final String TAG = "UploadWorker";

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 执行后台任务......
        LogHelper.get().e(TAG, "doWork: 执行Work()");
        return Result.success();
    }
}

