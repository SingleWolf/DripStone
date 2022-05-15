package com.walker.aop_optimize_plugin.utils

import java.util.concurrent.Executors

object Log {
    private val logThreadExecutor = Executors.newSingleThreadExecutor()

    fun log(log: Any?) {
        logThreadExecutor.submit {
            println("OPTIMIZE-ASM: $log")
        }
    }

}