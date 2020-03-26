package com.walker.core.exception;

/**
 * @date on 2018/3/30 0030 下午 13:46
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  应用异常监听者
 */
public interface OnCrashListener {
     void onTransact(Throwable e);
}
