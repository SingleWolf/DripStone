package com.walker.optimize.group.caton.blockcanary;

import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.walker.core.log.LogHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控IdleHandler卡顿
 */
public class IdleHandlerCanary {
    private static final String TAG = "IdleHandlerCanary";

    private static IdleHandlerCanary sInstance;

    public static IdleHandlerCanary get() {
        if (sInstance == null) {
            synchronized (IdleHandlerCanary.class) {
                if (sInstance == null) {
                    sInstance = new IdleHandlerCanary();
                }
            }
        }
        return sInstance;
    }

    private IdleHandlerCanary() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onCanary() {
        try {
            MessageQueue mainQueue = Looper.getMainLooper().getQueue();
            Field field = MessageQueue.class.getDeclaredField("mIdleHandlers");
            field.setAccessible(true);
            IdleHandlerArrayList<MessageQueue.IdleHandler> myIdleHandlerArrayList = new IdleHandlerArrayList<>();
            field.set(mainQueue, myIdleHandlerArrayList);
        } catch (Throwable t) {
            LogHelper.get().e(TAG, t.toString());
        }
    }

    private class IdleHandlerArrayList<T> extends ArrayList {
        Map<MessageQueue.IdleHandler, MyIdleHandler> map = new HashMap<>();

        @Override
        public boolean add(Object o) {
            if (o instanceof MessageQueue.IdleHandler) {
                MyIdleHandler myIdleHandler = new MyIdleHandler((MessageQueue.IdleHandler) o);
                map.put((MessageQueue.IdleHandler) o, myIdleHandler);
                return super.add(myIdleHandler);
            }
            return super.add(o);
        }

        @Override
        public boolean remove(@Nullable Object o) {
            if (o instanceof MyIdleHandler) {
                MessageQueue.IdleHandler idleHandler = ((MyIdleHandler) o).mIdleHandler;
                map.remove(idleHandler);
            } else {
                MyIdleHandler myIdleHandler = map.remove(o);
                if (myIdleHandler != null) {
                    return super.remove(myIdleHandler);
                }
            }
            return super.remove(o);
        }
    }

    private class MyIdleHandler implements MessageQueue.IdleHandler {
        // 卡顿阈值
        private long mBlockThresholdMillis = 3000;
        //采样频率
        private long mSampleInterval = 1000;
        private StackSampler mStackSampler;
        private MessageQueue.IdleHandler mIdleHandler;

        public MyIdleHandler(MessageQueue.IdleHandler idleHandler) {
            mIdleHandler = idleHandler;
        }

        @Override
        public boolean queueIdle() {
            mStackSampler = new StackSampler(mSampleInterval);
            mStackSampler.startDump();

            long startTime = System.currentTimeMillis();
            boolean keep = mIdleHandler.queueIdle();
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            LogHelper.get().i(TAG, "Run queueIdle and runTime = "+runTime);

            if (mBlockThresholdMillis < runTime) {
                LogHelper.get().e(TAG, "监测到卡顿，runTime = " + runTime);
                //获得卡顿时主线程堆栈
                List<String> stacks = mStackSampler.getStacks(startTime, endTime);
                for (String stack : stacks) {
                    LogHelper.get().e(TAG, stack);
                }
            }
            mStackSampler.stopDump();
            return keep;
        }
    }
}
