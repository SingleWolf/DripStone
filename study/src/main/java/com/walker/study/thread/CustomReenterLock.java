package com.walker.study.thread;

import com.walker.core.log.LogHelper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author Walker
 * @Date 2020-05-18 13:47
 * @Summary 基于AQS自定义显示锁(可重入锁)
 */
public class CustomReenterLock implements Lock {
    private static final String TAG = "CustomReenterLock";

    /**
     * 静态内部类，自定义同步器
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 判断占用状态
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() > 0;
        }

        /**
         * 获得锁,当state为0时获取锁
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            } else if (getExclusiveOwnerThread() == Thread.currentThread()) {
                //使得自定义锁变成可重入锁的核心操作
                setState(getState() + 1);
                return true;
            }
            return false;
        }

        /**
         * 释放锁，将state设置为0
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getExclusiveOwnerThread() != Thread.currentThread()) {
                throw new IllegalMonitorStateException();
            }
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setState(getState() - 1);
            if (getState() == 0) {
                setExclusiveOwnerThread(null);
            }
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
