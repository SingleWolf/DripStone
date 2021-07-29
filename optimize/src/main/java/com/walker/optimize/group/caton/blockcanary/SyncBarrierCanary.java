package com.walker.optimize.group.caton.blockcanary;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.walker.core.log.LogHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 同步屏障泄漏监听
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class SyncBarrierCanary {
    private static final String TAG = "SyncBarrierCanary";
    private static final int CODE_CHECK = 1;
    private static SyncBarrierCanary sInstance;
    private AtomicBoolean mIsRunning = new AtomicBoolean(false);
    private CheckHandler mCheckHandler;
    private SyncBarrierChecker mSyncBarrierChecker;

    public static SyncBarrierCanary get() {
        if (sInstance == null) {
            synchronized (SyncBarrierCanary.class) {
                if (sInstance == null) {
                    sInstance = new SyncBarrierCanary();
                }
            }
        }
        return sInstance;
    }

    private SyncBarrierCanary() {
    }

    public void onCanary() {
        if (!mIsRunning.getAndSet(true)) {
            HandlerThread handlerThread = new HandlerThread("SyncBarrier-Canary-Thread");
            handlerThread.start();
            mCheckHandler = new CheckHandler(handlerThread.getLooper());
            mSyncBarrierChecker = new SyncBarrierChecker();
            startCheck(0);
        }
    }

    private void startCheck(int delayTime) {
        if (delayTime < 0) {
            delayTime = 0;
        }
        mCheckHandler.sendEmptyMessageDelayed(CODE_CHECK, delayTime);
    }

    private class CheckHandler extends Handler {

        public CheckHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            switch (msg.what) {
                case CODE_CHECK:
                    checkLeak();
                    startCheck(16);
                    break;
                default:
                    break;
            }
        }
    }

    private void checkLeak() {
        if (mSyncBarrierChecker != null) {
            mSyncBarrierChecker.onCheck();
        }
    }

    /**
     * 同步屏障泄漏监测器
     * <p>
     * 不断轮询主线程Looper的MessageQueue的mMessage(也就是主线程当前正在处理的Message)。
     * 而SyncBarrier本身也是一种特殊的Message，其特殊在它的target是null。如果我们通过反射mMessage，发现当前的Message的target为null，
     * 并且通过这个Message的when发现其已经存在很久了，这个时候我们合理怀疑产生了SyncBarrier的泄漏（但还不能完全确定，如果因为其他原因导致主线程卡死，也可能会导致这种现象），
     * 然后再发送一个同步消息和一个异步消息，如果异步消息被处理了，但是同步消息一直无法被处理，这时候就说明产生了SyncBarrier的泄漏。
     * 如果激进一些，这个时候我们甚至可以反射调用MessageQueue的removeSyncBarrier方法，手动把这个SyncBarrier移除掉，从而从错误状态中恢复
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private class SyncBarrierChecker {
        private static final int CHECK_STRICTLY_MAX_COUNT = 3;
        private static final int DEFAULT_TIME_OUT = 3000;
        private int barrierCount = 0;
        private int timeOut = DEFAULT_TIME_OUT;
        private String currentMessageToString = "";

        private void startCheckLeaking(int token) {
            int checkCount = 0;
            barrierCount = 0;
            currentMessageToString = "";

            while (checkCount < CHECK_STRICTLY_MAX_COUNT) {
                checkCount++;
                int latestToken = getSyncBarrierToken();
                if (token != latestToken) {     //token变了，不是同一个barrier，return
                    LogHelper.get().i(TAG, "token变了，不是同一个barrier，latestToken=" + latestToken,false);
                    break;
                }
                if (isDetectSyncBarrierOnce()) {
                    LogHelper.get().e(TAG, "【同步屏障出现泄漏】 currentMessage=" + currentMessageToString);
                    LogHelper.get().e(TAG, getStackTraceInfo());
                    //发生了sync barrier泄漏
                    removeSyncBarrier(token);   //手动remove泄漏的sync barrier
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void removeSyncBarrier(int token) {
            try {
                MessageQueue mainQueue = Looper.getMainLooper().getQueue();
                Method method = mainQueue.getClass().getDeclaredMethod("removeSyncBarrier", int.class);
                method.setAccessible(true);
                method.invoke(mainQueue, token);
            } catch (Exception e) {
                LogHelper.get().e(TAG, e.getMessage());
            }
        }

        private boolean isDetectSyncBarrierOnce() {
            Message asyncMessage = Message.obtain();
            asyncMessage.setAsynchronous(true);
            asyncMessage.setTarget(mainHandler);
            asyncMessage.arg1 = 0;

            Message syncNormalMessage = Message.obtain();
            syncNormalMessage.arg1 = 1;

            mainHandler.sendMessage(asyncMessage);      //发送一个异步消息
            mainHandler.sendMessage(syncNormalMessage); //发送一个同步消息

            if (barrierCount > 3) {
                return true;
            }
            return false;
        }

        public void onCheck() {
            try {
                MessageQueue mainQueue = Looper.getMainLooper().getQueue();
                Field field = mainQueue.getClass().getDeclaredField("mMessages");
                field.setAccessible(true);
                Message mMessage = (Message) field.get(mainQueue);  //通过反射得到当前正在等待执行的Message
                if (mMessage != null) {
                    long when = SystemClock.uptimeMillis() - mMessage.getWhen();
                    if (timeOut < when && mMessage.getTarget() == null) { //target == null则为sync barrier
                        currentMessageToString = mMessage.toString();
                        int token = mMessage.arg1;
                        LogHelper.get().i(TAG, "Catch a syncBarrier , currentMessage=" + currentMessageToString);
                        startCheckLeaking(token);
                    }
                }
            } catch (Exception e) {
                LogHelper.get().e(TAG, e.getMessage());
            }
        }

        private int getSyncBarrierToken() {
            int token = -1;
            try {
                MessageQueue mainQueue = Looper.getMainLooper().getQueue();
                Field field = mainQueue.getClass().getDeclaredField("mMessages");
                field.setAccessible(true);
                Message mMessage = (Message) field.get(mainQueue);  //通过反射得到当前正在等待执行的Message
                Message p = mMessage;
                while (p != null && p.getTarget() != null) {
                    Field nextField = p.getClass().getDeclaredField("next");
                    nextField.setAccessible(true);
                    p = (Message) nextField.get(p);
                }
                if (p != null) {
                    token=p.arg1;
                }
            } catch (Exception e) {
                LogHelper.get().e(TAG, e.getMessage());
            } finally {
                return token;
            }
        }

        private String getStackTraceInfo() {
            StringBuilder stringBuilder = new StringBuilder();
            for (StackTraceElement stackTraceElement : Looper.getMainLooper().getThread().getStackTrace()) {
                stringBuilder
                        .append(stackTraceElement.toString())
                        .append("\r\n");
            }
            return stringBuilder.toString();
        }

        private Handler mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 0) {
                    barrierCount++;    //收到了异步消息，count++
                    LogHelper.get().i(TAG, "收到了异步消息, count=" + barrierCount,false);
                } else if (msg.arg1 == 1) {
                    barrierCount = 0;   //收到了同步消息，说明同步屏障不在, count设置为0
                    LogHelper.get().i(TAG, "收到了同步消息，说明同步屏障不在, count设置为0",false);
                }
            }
        };
    }
}
