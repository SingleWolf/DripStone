package com.walker.common.log;

import android.content.Context;
import android.os.Environment;

import com.walker.core.log.ILogger;
import com.walker.log.BuildConfig;
import com.walker.log.xlog.Log;
import com.walker.log.xlog.Options;
import com.walker.log.xlog.Xlog;

public class XLogger implements ILogger {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public XLogger(Context context) {
        String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
        String logPath = SDCARD + "/DripStone/logDir";
        // this is necessary, or may crash for SIGBUS
        String cachePath = context.getFilesDir() + "/xlog";
        String pubkey = "9b40b6c9ad693aba1651f6bd294efca3701272056c8803fc8626ba411fc1d6887f5c1936c72bbe0a29de05da8f363b9535ee7e231ee62d603edeed65e23dd836";
        String logFileName = "drip";
        Options options = new Options();
        options.setNamePrefix(logFileName);
        options.setCacheDir(cachePath);
        options.setLogDir(logPath);
        options.setLoadLib(true);
        options.setPubkey(pubkey);
        options.setMode(Xlog.AppednerModeAsync);
        options.setMaxFileSize(MAX_FILE_SIZE);
        if (BuildConfig.DEBUG) {
            options.setConsoleLogOpen(true);
            options.setLevel(Xlog.LEVEL_DEBUG);
        } else {
            options.setConsoleLogOpen(true);
            options.setLevel(Xlog.LEVEL_INFO);
        }
        Xlog xlog = new Xlog();
        Log.setLogImp(xlog);
        Xlog.open(options);
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
        Log.appenderFlush(false);
    }

    @Override
    public void close() {
        Log.appenderClose();
    }
}
