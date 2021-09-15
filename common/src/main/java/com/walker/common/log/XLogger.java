package com.walker.common.log;

import android.content.Context;
import android.os.Environment;

import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;
import com.walker.core.BuildConfig;
import com.walker.core.log.ILogger;

public class XLogger implements ILogger {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public XLogger(Context context) {
        String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        String logPath = SDCARD + "/DripStone/logDir";
        // this is necessary, or may crash for SIGBUS
        String cachePath = context.getFilesDir() + "/xlog";
        String pubkey = "9b40b6c9ad693aba1651f6bd294efca3701272056c8803fc8626ba411fc1d6887f5c1936c72bbe0a29de05da8f363b9535ee7e231ee62d603edeed65e23dd836";
        String logFileName = "drip";
        Xlog xlog = new Xlog();
        Log.setLogImp(xlog);
        if (BuildConfig.DEBUG) {
            Xlog.open(true, Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath, logFileName, pubkey);
            Log.setConsoleLogOpen(true);
        } else {
            Xlog.open(true, Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, logFileName, pubkey);
            Log.setConsoleLogOpen(false);
        }
        Log.getImpl().setMaxFileSize(0, MAX_FILE_SIZE);
    }

    @Override
    public void d(String tag, String message, boolean isSave) {
        Log.d(tag, message);
    }

    @Override
    public void i(String tag, String message, boolean isSave) {
        Log.i(tag, message);
    }

    @Override
    public void w(String tag, String message, boolean isSave) {
        Log.w(tag, message);
    }

    @Override
    public void e(String tag, String message, boolean isSave) {
        Log.e(tag, message);
    }

    @Override
    public void start() {

    }

    @Override
    public void flush() {
        Log.appenderFlush();
    }

    @Override
    public void close() {
        Log.appenderClose();
    }
}
