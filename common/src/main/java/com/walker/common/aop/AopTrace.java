package com.walker.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author  : walker
 * Date    : 2021/8/24  2:19 下午
 * Email   : feitianwumu@163.com
 * Summary :AopTrace
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AopTrace {
    Class<? extends AopInvocation> point();
}
