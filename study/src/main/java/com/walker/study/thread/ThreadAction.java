package com.walker.study.thread;

import com.walker.core.log.LogHelper;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author Walker
 * @Date 2020-05-15 09:37
 * @Summary 线程相关操作
 */
public class ThreadAction {
    private static final String TAG = "ThreadAction";
    private static ThreadAction sInstance;

    private ThreadAction() {
    }

    public static ThreadAction getInstance() {
        if (sInstance == null) {
            synchronized (ThreadAction.class) {
                if (sInstance == null) {
                    sInstance = new ThreadAction();
                }
            }
        }
        return sInstance;
    }

    /**
     * 线程的开启（共两种方式：Thread和Runnable）
     * <p>
     * Callable方式归根结底是通过Runnable实现的
     */
    public void start() {
        LogHelper.get().i(TAG, "------开启线程--------");
        new StartThread().start();
        new Thread(new StartRunnable()).start();
        StartCallable startCallable = new StartCallable();
        FutureTask<String> futureTask = new FutureTask<>(startCallable);
        new Thread(futureTask).start();
        try {
            Thread.sleep(1000);
            LogHelper.get().i(TAG, "StartCallable运行结果：" + futureTask.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class StartThread extends Thread {
        @Override
        public void run() {
            super.run();
            LogHelper.get().i(TAG, "继承Thread方式开启线程");
        }
    }

    class StartRunnable implements Runnable {

        @Override
        public void run() {
            LogHelper.get().i(TAG, "实现Runnable方式开启线程");
        }
    }

    class StartCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            LogHelper.get().i(TAG, "实现Callable方式开启线程");
            return "Callable方式归根结底是通过Runnable实现的,Time is " + System.currentTimeMillis();
        }
    }

    /**
     * 终止线程
     * <p>
     * 终止线程一定不要用stop。 stop是抢占式的，太粗暴，容易出问题
     * <p>
     * 用interrupt向线程发送中断信号，interrupt是协作式的
     */
    public void stop() {
        LogHelper.get().i(TAG, "------StopThread->start()------");
        StopThread stopThread = new StopThread();
        stopThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stopThread.interrupt();
            LogHelper.get().i(TAG, "------StopThread->interrupt()------");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LogHelper.get().i(TAG, "------StopRunnable->start()------");
        Thread stopThread2 = new Thread(new StopRunnable());
        stopThread2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stopThread2.interrupt();
            LogHelper.get().i(TAG, "------StopRunnable->interrupt()------");
        }
    }

    class StopThread extends Thread {
        @Override
        public void run() {
            super.run();
            int i = 0;
            while (!isInterrupted()) {
                LogHelper.get().i(TAG, "StopThread->i:" + ++i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //sleep在抛出InterruptedException异常时，在捕获异常之前会清除中断信号的标记
                    interrupt();
                }
            }
        }
    }

    class StopRunnable implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (!Thread.interrupted()) {
                LogHelper.get().i(TAG, "StopRunnable->i:" + ++i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //sleep在抛出InterruptedException异常时，在捕获异常之前会清除中断信号的标记
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 产生死锁问题
     * <p>
     * 产生条件：
     * 1）多(M)个操作者获取多(N)个资源,N<=M
     * 2) 使用资源的顺序不对
     * 3）抢占资源后，不释放资源
     */
    public void genDeadLock() {
        LogHelper.get().i(TAG, "------genDeadLock------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("DeadLock-Thread-1");
                synchronized (lock_1) {
                    LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_1");
                    synchronized (lock_2) {
                        LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_2");
                        LogHelper.get().i(TAG, Thread.currentThread().getName() + "达到目的，吃到水蜜桃了");
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("DeadLock-Thread-2");
                synchronized (lock_2) {
                    LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_2");
                    synchronized (lock_1) {
                        LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_1");
                        LogHelper.get().i(TAG, Thread.currentThread().getName() + "达到目的，吃到水蜜桃了");
                    }
                }
            }
        }).start();
    }

    private Object lock_1 = new Object();
    private Object lock_2 = new Object();

    /**
     * 解决死锁问题
     * <p>
     * 利用显示锁的尝试拿锁机制、释放锁资源，解决死锁问题
     */
    public void solveDeadLock() {
        LogHelper.get().i(TAG, "------solveDeadLock------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("DeadLock-Thread-3");
                Random random = new Random();
                while (true) {
                    if (lock_3.tryLock()) {
                        LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_3");
                        try {
                            if (lock_4.tryLock()) {
                                LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_4");
                                try {
                                    LogHelper.get().i(TAG, Thread.currentThread().getName() + "达到目的，吃到水蜜桃了");
                                    break;
                                } finally {
                                    lock_4.unlock();
                                }
                            }
                        } finally {
                            lock_3.unlock();
                        }
                    }
                    //稍微休眠片刻，以避免产生活锁问题
                    try {
                        Thread.sleep(random.nextInt(3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("DeadLock-Thread-4");
                Random random = new Random();
                while (true) {
                    if (lock_4.tryLock()) {
                        LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_4");
                        try {
                            if (lock_3.tryLock()) {
                                LogHelper.get().i(TAG, Thread.currentThread().getName() + "获取锁lock_3");
                                try {
                                    LogHelper.get().i(TAG, Thread.currentThread().getName() + "达到目的，吃到水蜜桃了");
                                    break;
                                } finally {
                                    lock_3.unlock();
                                }
                            }
                        } finally {
                            lock_4.unlock();
                        }
                    }
                    //稍微休眠片刻，以避免产生活锁问题
                    try {
                        Thread.sleep(random.nextInt(3));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Lock lock_3 = new ReentrantLock();
    private Lock lock_4 = new ReentrantLock();

    /**
     * 让线程有序执行（利用join实现）
     * <p>
     * 可用join、单核线程池、CountDownLatch实现
     */
    public void orderRun() {
        LogHelper.get().i(TAG, "------orderRun------");
        Thread orderThread_1 = new Thread(new Runnable() {
            @Override
            public void run() {
                LogHelper.get().i(TAG, Thread.currentThread().getName() + "执行");
            }
        });
        Thread orderThread_2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    orderThread_1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogHelper.get().i(TAG, Thread.currentThread().getName() + "执行");
            }
        });
        Thread orderThread_3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    orderThread_2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LogHelper.get().i(TAG, Thread.currentThread().getName() + "执行");

            }
        });
        orderThread_1.setName("OrderThread-1");
        orderThread_2.setName("OrderThread-2");
        orderThread_3.setName("OrderThread-3");

        orderThread_1.start();
        orderThread_2.start();
        orderThread_3.start();
    }
}
