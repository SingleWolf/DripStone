package com.walker.optimize.group.trace;
/**
 * @Author Walker
 *
 * @Date   2020-05-08 16:32
 *
 * @Summary MethodObserver
 */
public interface MethodObserver {
    default void onMethodEnter(String tag, String methodName) {
    }

    default void onMethodExit(String tag, String methodName) {
    }
}
