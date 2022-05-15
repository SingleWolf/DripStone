package com.walker.aop_optimize_plugin.plugins.thread

open class ShadowThreadConfig(
    private val shadowThreadClass: String = "com.walker.optimize.group.oom.thread.ShadowThread",
    private val shadowThreadPoolClass: String = "com.walker.optimize.group.oom.thread.ShadowExecutors",
    val threadHookPointList: List<ThreadHookPoint> = threadHookPoints,
) {
    val formatShadowThreadClass: String
        get() = shadowThreadClass.replace(".", "/")

    val formatShadowThreadPoolClass: String
        get() = shadowThreadPoolClass.replace(".", "/")
}

private val threadHookPoints = listOf(
    ThreadHookPoint(
        methodName = "newFixedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadExecutor"
    ),
    ThreadHookPoint(
        methodName = "newCachedThreadPool"
    ),
    ThreadHookPoint(
        methodName = "newSingleThreadScheduledExecutor"
    ),
    ThreadHookPoint(
        methodName = "newScheduledThreadPool"
    ),
)

data class ThreadHookPoint(
    val methodName: String
)
