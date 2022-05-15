package com.walker.optimize.group.lancet;

import com.walker.core.log.LogHelper;
import com.walker.optimize.group.oom.thread.ShadowThread;

public class LancetTest {
    public static void testError(String msg) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogHelper.get().i("LancetTest", "testError");
            }
        }).start();

//        new ShadowThread(new Runnable() {
//            @Override
//            public void run() {
//                LogHelper.get().i("LancetTest", "ShadowThread");
//            }
//        }).start();

//        int a = 1 / 0;
//        LogHelper.get().i("LancetTest", msg + a);
    }

}
