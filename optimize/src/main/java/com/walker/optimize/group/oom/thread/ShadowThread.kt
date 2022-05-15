package com.walker.optimize.group.oom.thread

import android.util.Log
import java.util.concurrent.atomic.AtomicInteger

class ShadowThread(runnable: Runnable?, name: String?, className: String) :
    Thread(runnable, generateThreadName(name, className)) {

    companion object {

        private val threadId = AtomicInteger(0)

        private fun generateThreadName(name: String?, className: String): String {
            return className + "-" + threadId.getAndIncrement() + if (name.isNullOrBlank()) {
                ""
            } else {
                "-$name"
            }
        }

    }

    constructor(runnable: Runnable, className: String) : this(runnable, null, className)

    constructor(name: String, className: String) : this(null, name, className)

    @Synchronized
    override fun start() {
        ShadowExecutors.newFixedThreadPool(5, "ShadowThread")
            .execute(ShadowRunnable(name, Runnable {
                this.run()
            }))
    }

    class ShadowRunnable(var name: String, var task: Runnable) : Runnable {
        override fun run() {
            try {
                Log.i("ShadowThread", "run name = $name")
                task.run()
            } catch (e: Exception) {
                Log.e(
                    "ShadowThread",
                    "name=" + name + ", exception:" + e.message
                )
                val exception = RuntimeException("threadName=" + name + ", exception:" + e.message)
                exception.stackTrace = e.stackTrace
                throw exception
            }
        }
    }

}