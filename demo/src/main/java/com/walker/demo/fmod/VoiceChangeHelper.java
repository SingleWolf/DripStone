package com.walker.demo.fmod;

import android.content.Context;

import com.walker.core.util.ExecutorUtils;

import org.fmod.FMOD;

/**
 * @Author Walker
 * @Date 2021/4/22 3:30 PM
 * @Summary 变音辅助类
 */
public class VoiceChangeHelper {
    public static final int MODE_NORMAL = 0; // 正常
    public static final int MODE_LUOLI = 1; //
    public static final int MODE_DASHU = 2; //
    public static final int MODE_JINGSONG = 3; //
    public static final int MODE_GAOGUAI = 4; //
    public static final int MODE_KONGLING = 5; //

    private static volatile VoiceChangeHelper sInstance;
    private Voice mVoice;

    private VoiceChangeHelper() {
        mVoice = new Voice();
    }

    public static VoiceChangeHelper get() {
        if (sInstance == null) {
            synchronized (VoiceChangeHelper.class) {
                if (sInstance == null) {
                    sInstance = new VoiceChangeHelper();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        FMOD.init(context.getApplicationContext());
    }

    public void unInit() {
        FMOD.close();
    }

    public void changeVoice(String path, int mode) {
        ExecutorUtils.executeTask(() -> mVoice.changeVoice(path, mode));
    }
}
