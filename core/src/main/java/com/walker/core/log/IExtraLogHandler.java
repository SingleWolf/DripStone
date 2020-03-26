package com.walker.core.log;

/**
 * @Author Walker
 * @Date 2020-03-26 14:42
 * @Summary 额外处理日志的接口
 */
public interface IExtraLogHandler {
    void onTransact(String tag,String log);
}
