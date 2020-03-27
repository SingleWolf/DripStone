package com.walker.network.retrofit.base;

import android.app.Application;

public interface INetworkRequiredInfo {
    String getOS();
    String getAppVersionName();
    String getAppVersionCode();
    boolean isDebug();
    Application getApplicationContext();
}
