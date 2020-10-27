package com.walker.optimize.group.network.netspeed;

/**
 * @Author Walker
 * @Date 2020/10/26 1:40 PM
 * @Summary 网速监测
 */
public interface INetSpeedMonitor {
    void start();
    void stop();
    void register();
    void unregister();
}
