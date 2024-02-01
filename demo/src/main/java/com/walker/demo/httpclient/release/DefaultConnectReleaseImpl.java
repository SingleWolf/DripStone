package com.walker.demo.httpclient.release;

import com.walker.core.log.LogHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: Walker
 * email: feitianwumu@163.com
 * date: 2023/7/11 10:50
 * desc: 默认的连接释放实现
 */
public class DefaultConnectReleaseImpl implements IConnectRelease {

    private static final String TAG = "DefaultConnectReleaseImpl";
    private ConcurrentHashMap<String, IConnectMgr> connectMgrMap = new ConcurrentHashMap<>();

    private final long MAX_TIME = 5 * 60 * 1000;

    @Override
    public void put(String tag, ConnectClient client) {
        IConnectMgr connectMgr = connectMgrMap.get(tag);
        if (connectMgr != null) {
            connectMgr.put(client);
        } else {
            DefaultConnectMgr newConnectMgr = new DefaultConnectMgr();
            newConnectMgr.put(client);
            connectMgrMap.put(tag, newConnectMgr);
        }
    }

    @Override
    public synchronized void release(String tag) {
        releaseByCheckTime();
        if (connectMgrMap.containsKey(tag)) {
            IConnectMgr connectMgr = connectMgrMap.get(tag);
            if (connectMgr != null) {
                connectMgr.release(this, 1);
            }
        }
    }

    private void releaseByCheckTime() {
        long currentTime = System.currentTimeMillis();
        for (IConnectMgr connectMgr : connectMgrMap.values()) {
            if (Math.abs(currentTime - connectMgr.getTimeStamp()) > MAX_TIME) {
                connectMgr.release(this, 0);
            }
        }
    }

    @Override
    public void remove(IConnectMgr connectMgr) {
        LogHelper.get().i(TAG, "remove()");
        if (connectMgrMap.contains(connectMgr)) {
            connectMgrMap.remove(connectMgr);
        }
    }

    @Override
    public IConnectMgr getConnectMgr(String tag) {
        return connectMgrMap.get(tag);
    }

    @Override
    public Map<String, IConnectMgr> listConnectMgr() {
        return connectMgrMap;
    }
}
