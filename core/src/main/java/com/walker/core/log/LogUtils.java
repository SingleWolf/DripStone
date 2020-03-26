package com.walker.core.log;

/**
 * @Author Walker
 * @Date 2020-03-26 15:58
 * @Summary 日志工具类
 */
public class LogUtils {
    /**
     * 初始化
     *
     * @param level           日志等级
     * @param logger          文件打印器
     * @param extraLogHandler 额外处理回调
     */
    public static void init(int level, BaseLogger logger, IExtraLogHandler extraLogHandler) {
        LogHelper.get().setLevel(level).setLogger(logger).setExtraLogHandler(extraLogHandler);
    }

    /**
     * 打印调试信息
     *
     * @param message 信息
     */

    public static void d(String tag, String message) {
        LogHelper.get().d(tag, message, false, false);
    }


    /**
     * 打印重要数据
     *
     * @param message 信息
     */

    public static void i(String tag, String message) {
        LogHelper.get().i(tag, message, false, false);
    }


    /**
     * 打印警告信息
     *
     * @param message 信息
     */

    public static void w(String tag, String message) {
        LogHelper.get().w(tag, message, false, false);
    }


    /**
     * 打印错误信息
     *
     * @param message 信息
     */

    public static void e(String tag, String message) {
        LogHelper.get().e(tag, message, false, false);
    }

    public static void d(String tag, String message, boolean isSave) {
        LogHelper.get().d(tag, message, isSave, false);
    }

    public static void i(String tag, String message, boolean isSave) {
        LogHelper.get().i(tag, message, isSave, false);
    }

    public static void w(String tag, String message, boolean isSave) {
        LogHelper.get().w(tag, message, isSave, false);
    }

    public static void e(String tag, String message, boolean isSave) {
        LogHelper.get().e(tag, message, isSave, false);
    }

}
