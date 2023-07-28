package com.walker.crash;

public class NativeAirbagHelper {

    static {
        System.loadLibrary("bugly");
    }

    public static native String stringFromJNI();

    public static native void openNativeAirbag(int signal,String soName,String backtrace);

    public static native void testNativeCrash_1();

    public static native void testNativeCrash_2();
}
