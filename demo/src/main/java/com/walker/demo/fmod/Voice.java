package com.walker.demo.fmod;

import com.walker.common.aop.AopTrace;
import com.walker.core.log.LogHelper;
import com.walker.demo.CheckOut;

public class Voice {
    private static final int MODE_NORMAL = 0; // 正常
    private static final int MODE_LUOLI = 1; //
    private static final int MODE_DASHU = 2; //
    private static final int MODE_JINGSONG = 3; //
    private static final int MODE_GAOGUAI = 4; //
    private static final int MODE_KONGLING = 5; //

    static {
        System.loadLibrary("VoiceChange");
    }

    @AopTrace(point = CheckOut.class)
    public void changeVoice(String path, int mode) {
        voiceChangeNative(mode, path);
    }

    private native void voiceChangeNative(int modeNormal, String path);

    // 给C++调用的函数
    // JNI 调用 Java函数的时候，忽略掉 私有、公开 等
    private void playerEnd(String msg) {
        LogHelper.get().d("Voice", msg);
    }
}
