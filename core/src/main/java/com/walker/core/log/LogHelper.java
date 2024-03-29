package com.walker.core.log;

import android.text.TextUtils;

/**
 * @Author Walker
 * @Date 2020-03-26 14:16
 * @Summary 日志打印
 */
public class LogHelper {
    public static final String TAG = "LogHelper";
    /**
     * 选择的最低打印级别
     */
    private int mMinLevel = LogLevel.DEBUG;

    private ILogger mLogger;

    private IExtraLogHandler mExtraLogHandler;

    private static LogHelper sInstance = new LogHelper();

    public static LogHelper get() {
        return sInstance;
    }

    public LogHelper setLevel(int level) {
        mMinLevel = level;
        return this;
    }

    public LogHelper setLogger(ILogger logger) {
        mLogger = logger;
        return this;
    }

    public LogHelper setExtraLogHandler(IExtraLogHandler extraLogHandler) {
        mExtraLogHandler = extraLogHandler;
        return this;
    }

    public void config() {
        if (mLogger != null) {
            mLogger.start();
        }
    }

    /**
     * 打印调试信息
     *
     * @param message 信息
     */

    public void d(String tag, String message) {
        d(tag, message, false, false);
    }


    /**
     * 打印重要数据
     *
     * @param message 信息
     */

    public void i(String tag, String message) {
        i(tag, message, false, false);
    }


    /**
     * 打印警告信息
     *
     * @param message 信息
     */

    public void w(String tag, String message) {
        w(tag, message, false, false);
    }


    /**
     * 打印错误信息
     *
     * @param message 信息
     */

    public void e(String tag, String message) {
        e(tag, message, false, false);
    }

    public void d(String tag, String message, boolean isSave) {
        d(tag, message, isSave, false);
    }

    public void i(String tag, String message, boolean isSave) {
        i(tag, message, isSave, false);
    }

    public void w(String tag, String message, boolean isSave) {
        w(tag, message, isSave, false);
    }

    public void e(String tag, String message, boolean isSave) {
        e(tag, message, isSave, false);
    }


    public void d(String tag, String message, boolean isSave, boolean isExtra) {
        if (mMinLevel <= LogLevel.DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            try {
                String tagLabel = String.format("%s(%s)", TAG, tag);
                mLogger.d(tagLabel, message, isSave);

                if (isExtra) {
                    exeExtraLog(tagLabel, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void i(String tag, String message, boolean isSave, boolean isExtra) {
        if (mMinLevel <= LogLevel.INFO) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            try {
                String tagLabel = String.format("%s(%s)", TAG, tag);
                mLogger.i(tagLabel, message, isSave);
                if (isExtra) {
                    exeExtraLog(tagLabel, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void w(String tag, String message, boolean isSave, boolean isExtra) {
        if (mMinLevel <= LogLevel.WARN) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            try {
                String tagLabel = String.format("%s(%s)", TAG, tag);
                mLogger.w(tagLabel, message, isSave);

                if (isExtra) {
                    exeExtraLog(tagLabel, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void e(String tag, String message, boolean isSave, boolean isExtra) {
        if (mMinLevel <= LogLevel.ERROR) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            try {
                String tagLabel = String.format("%s(%s)", TAG, tag);
                mLogger.e(tagLabel, message, isSave);

                if (isExtra) {
                    exeExtraLog(tagLabel, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void exeExtraLog(String tagLabel, String message) {
        if (mExtraLogHandler != null) {
            mExtraLogHandler.onTransact(tagLabel, message);
        }
    }

    public void flush(){
        if (mLogger != null) {
            mLogger.flush();
        }
    }

    public void close() {
        if (mLogger != null) {
            mLogger.close();
        }
    }
}
