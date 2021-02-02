package com.walker.optimize.group.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Walker
 *
 * @Date   2020-05-08 16:30
 *
 * @Summary TraceMethod
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface TraceMethod {
    String tag();
}
