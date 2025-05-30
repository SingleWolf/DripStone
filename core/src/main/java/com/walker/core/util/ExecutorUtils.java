package com.walker.core.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Walker
 * @date on 2018/4/11 0011 下午 17:37
 * @email feitianwumu@163.com
 * @desc 利用线程池重复利用资源，异步执行任务Runnable，减小创建线程开销
 */
public class ExecutorUtils {
    private static ScheduledThreadPoolExecutor gScheduledThreadPoolExecutor = null;
    private static Handler gMainHandler = null;

    //不包含网络传输处理过程的线程池执行对象
    private static ThreadPoolExecutor gThreadPoolExecutor = null;

    //包含网络传输处理过程的线程池执行对象
    private static ThreadPoolExecutor gNetProcessThreadPoolExecutor = null;

    //执行不包含网络传输处理过程的线程
    public static void executeTask(Runnable task) {
        ensureThreadPoolExecutor();
        gThreadPoolExecutor.execute(task);
    }

    //执行包含网络传输处理过程的线程，可能存在等待阻塞的状况
    public static void executeNetTask(Runnable task) {
        ensureNetProcessThreadPoolExecutor();
        gNetProcessThreadPoolExecutor.execute(task);
    }

    public static <T> Future<T> submitTask(Callable<T> task) {
        ensureThreadPoolExecutor();
        return gThreadPoolExecutor.submit(task);
    }

    public static void scheduleTask(long delay, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        gScheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public static void scheduleTaskAtFixedRateIgnoringTaskRunningTime(long initialDelay, long period, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        gScheduledThreadPoolExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static void scheduleTaskAtFixedRateIncludingTaskRunningTime(long initialDelay, long period, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        gScheduledThreadPoolExecutor.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static void scheduleTaskOnUiThread(long delay, Runnable task) {
        ensureMainHandler();
        gMainHandler.postDelayed(task, delay);
    }

    public static void runTaskOnUiThread(Runnable task) {
        ensureMainHandler();
        gMainHandler.post(task);
    }

    private static void ensureMainHandler() {
        if (gMainHandler == null) {
            gMainHandler = new Handler(Looper.getMainLooper());
        }
    }

    private static void ensureThreadPoolExecutor() {
        if (gThreadPoolExecutor == null) {
            gThreadPoolExecutor = new ThreadPoolExecutor(5, 10,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    Executors.defaultThreadFactory());
            //gThreadPoolExecutor.allowCoreThreadTimeOut(true);
        }
    }

    private static void ensureNetProcessThreadPoolExecutor() {
        if (gNetProcessThreadPoolExecutor == null) {
            gNetProcessThreadPoolExecutor = new ThreadPoolExecutor(10, 15,
                    60L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    Executors.defaultThreadFactory());
            gNetProcessThreadPoolExecutor.allowCoreThreadTimeOut(true);
        }
    }

    private static void ensureScheduledThreadPoolExecutor() {
        if (gScheduledThreadPoolExecutor == null) {
            gScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
            gScheduledThreadPoolExecutor.allowCoreThreadTimeOut(true);
        }
    }

    public static void shutdown() {
        if (gThreadPoolExecutor != null) {
            gThreadPoolExecutor.shutdown();
            gThreadPoolExecutor = null;
        }

        if (gScheduledThreadPoolExecutor != null) {
            gScheduledThreadPoolExecutor.shutdown();
            gScheduledThreadPoolExecutor = null;
        }
    }
}
