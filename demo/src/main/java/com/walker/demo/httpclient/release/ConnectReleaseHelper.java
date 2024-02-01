package com.walker.demo.httpclient.release;

import java.util.Map;

/**
 * author: Walker
 * email: feitianwumu@163.com
 * date: 2023/7/11 09:57
 * desc: httpClient连接释放管理器
 */
public class ConnectReleaseHelper implements IConnectRelease {

    private static volatile ConnectReleaseHelper sInstance;

    private IConnectRelease connectReleaseProxy = new DefaultConnectReleaseImpl();

    public static ConnectReleaseHelper get() {
        if (sInstance == null) {
            synchronized (ConnectReleaseHelper.class) {
                if (sInstance == null) {
                    sInstance = new ConnectReleaseHelper();
                }
            }
        }
        return sInstance;
    }

    private ConnectReleaseHelper() {
    }


    @Override
    public void put(String tag, ConnectClient client) {
        connectReleaseProxy.put(tag, client);
    }

    @Override
    public void release(String tag) {
        connectReleaseProxy.release(tag);
    }

    @Override
    public void remove(IConnectMgr connectMgr) {
        connectReleaseProxy.remove(connectMgr);
    }

    @Override
    public IConnectMgr getConnectMgr(String tag) {
        return connectReleaseProxy.getConnectMgr(tag);
    }

    @Override
    public Map<String, IConnectMgr> listConnectMgr() {
        return connectReleaseProxy.listConnectMgr();
    }
}
