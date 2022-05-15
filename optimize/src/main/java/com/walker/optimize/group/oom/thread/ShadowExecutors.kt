package com.walker.optimize.group.oom.thread

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

object ShadowExecutors {
    private const val defaultThreadKeepAliveTime = 5000L

    @JvmStatic
    fun newFixedThreadPool(nThreads: Int, className: String): ExecutorService {
        return newFixedThreadPool(nThreads, null, className)
    }

    @JvmStatic
    fun newFixedThreadPool(
        nThreads: Int,
        threadFactory: ThreadFactory?,
        className: String
    ): ExecutorService {
        return getOptimizedExecutorService(
            nThreads, nThreads,
            0L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(),
            threadFactory, className
        )
    }

    @JvmStatic
    fun newSingleThreadExecutor(className: String): ExecutorService {
        return newSingleThreadExecutor(null, className)
    }

    @JvmStatic
    fun newSingleThreadExecutor(
        threadFactory: ThreadFactory?,
        className: String
    ): ExecutorService {
        return getOptimizedExecutorService(
            1, 1,
            0L, TimeUnit.MILLISECONDS,
            LinkedBlockingQueue(),
            threadFactory, className
        )
    }

    @JvmStatic
    fun newCachedThreadPool(className: String): ExecutorService {
        return newCachedThreadPool(null, className)
    }

    @JvmStatic
    fun newCachedThreadPool(threadFactory: ThreadFactory?, className: String): ExecutorService {
        return getOptimizedExecutorService(
            0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            SynchronousQueue(),
            threadFactory, className
        )
    }

    @JvmStatic
    fun newSingleThreadScheduledExecutor(className: String): ScheduledExecutorService {
        return newSingleThreadScheduledExecutor(null, className)
    }

    @JvmStatic
    fun newSingleThreadScheduledExecutor(
        threadFactory: ThreadFactory?,
        className: String
    ): ScheduledExecutorService {
        return getOptimizedScheduledExecutorService(1, threadFactory, className)
    }

    @JvmStatic
    fun newScheduledThreadPool(corePoolSize: Int, className: String): ScheduledExecutorService {
        return newScheduledThreadPool(corePoolSize, null, className)
    }

    @JvmStatic
    fun newScheduledThreadPool(
        corePoolSize: Int,
        threadFactory: ThreadFactory?,
        className: String
    ): ScheduledExecutorService {
        return getOptimizedScheduledExecutorService(corePoolSize, threadFactory, className)
    }

    private fun getOptimizedExecutorService(
        corePoolSize: Int,
        maximumPoolSize: Int,
        keepAliveTime: Long,
        unit: TimeUnit,
        workQueue: BlockingQueue<Runnable>,
        threadFactory: ThreadFactory? = null,
        className: String,
    ): ExecutorService {
        val executor = ThreadPoolExecutor(
            corePoolSize, maximumPoolSize,
            keepAliveTime, unit,
            workQueue,
            NamedThreadFactory(threadFactory, className)
        )
        executor.setKeepAliveTime(defaultThreadKeepAliveTime, TimeUnit.MILLISECONDS)
        executor.allowCoreThreadTimeOut(true)
        return executor
    }

    private fun getOptimizedScheduledExecutorService(
        threadSize: Int,
        threadFactory: ThreadFactory? = null,
        className: String
    ): ScheduledExecutorService {
        val executor = ScheduledThreadPoolExecutor(
            threadSize,
            NamedThreadFactory(threadFactory, className)
        )
        executor.setKeepAliveTime(defaultThreadKeepAliveTime, TimeUnit.MILLISECONDS)
        executor.allowCoreThreadTimeOut(true)
        return executor
    }

    private class NamedThreadFactory(
        private val threadFactory: ThreadFactory?,
        private val className: String
    ) : ThreadFactory {

        private val threadId = AtomicInteger(0)

        override fun newThread(runnable: Runnable): Thread {
            val originThread = threadFactory?.newThread(runnable)
            val threadName =
                className + "-" + threadId.getAndIncrement() + if (originThread != null) {
                    "-" + originThread.name
                } else {
                    ""
                }
            val thread = originThread ?: Thread(runnable)
            thread.name = threadName
            if (thread.isDaemon) {
                thread.isDaemon = false
            }
            if (thread.priority != Thread.NORM_PRIORITY) {
                thread.priority = Thread.NORM_PRIORITY
            }
            return thread
        }

    }

}