package com.walker.study.thread;

import com.walker.core.log.LogHelper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author Walker
 * @Date 2020-05-18 13:47
 * @Summary 基于AQS自定义显示锁(独占锁)
 */
public class CustomLock implements Lock {
    private static final String TAG = "CustomLock";

    /**
     * 静态内部类，自定义同步器
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 判断占用状态
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 获得锁
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 释放锁
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setState(0);
            setExclusiveOwnerThread(null);
            return true;
        }

        /**
         * 返回一个Condition。每个condition都包含一个condition队列
         */
        Condition newCondition() {
            return new ConditionObject();
        }
    }

    /**
     * 同步器
     */
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        LogHelper.get().i(TAG, Thread.currentThread().getName() + " ready get lock");
        sync.acquire(1);
        LogHelper.get().i(TAG, Thread.currentThread().getName() + " already got lock");
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        LogHelper.get().i(TAG, Thread.currentThread().getName() + " ready release lock");
        sync.release(1);
        LogHelper.get().i(TAG, Thread.currentThread().getName() + " already released lock");
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

}
