package com.walker.common.aop;

/**
 * Author  : walker
 * Date    : 2021/8/24  2:24 下午
 * Email   : feitianwumu@163.com
 * Summary : AopInvocation
 */
public interface AopInvocation {
    void onBeforeMethod();

    void onAfterMethod();
}
