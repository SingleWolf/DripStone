package com.walker.optimize.group.lancet;

import com.walker.core.log.LogHelper;

public class LancetTest {
    public static void testError(String msg) {
        int a = 1 / 0;
        LogHelper.get().i("LancetTest", msg + a);
    }

}
