package com.example.plugintest;

public class NativeTest {

    static {
        System.loadLibrary("NativeTest");
    }

    public native String getInfoFromNative();

    public String getInfo() {
        return getInfoFromNative();
    }
}
