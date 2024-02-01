package com.walker.demo.httpclient.release;

public interface IConnectMgr {

    void setTimeStamp(long time);

    long getTimeStamp();

    void release(IConnectRelease connectRelease,int leastNum);

    void put(ConnectClient client);
}
