package com.walker.demo.httpclient.release;

import java.util.Map;

public interface IConnectRelease {

    void put(String tag, ConnectClient client);

    void release(String tag);

    void remove(IConnectMgr connectMgr);

    IConnectMgr getConnectMgr(String tag);

    Map<String, IConnectMgr> listConnectMgr();
}
