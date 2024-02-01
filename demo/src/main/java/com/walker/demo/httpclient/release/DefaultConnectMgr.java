package com.walker.demo.httpclient.release;


import com.walker.core.log.LogHelper;

import java.util.Deque;
import java.util.LinkedList;

/**
 * author: Walker
 * email: feitianwumu@163.com
 * date: 2023/7/11 10:52
 * desc: 默认的连接管理
 */
public class DefaultConnectMgr implements IConnectMgr {
    private long lastTimeStamp = 0;

    private Deque<ConnectClient> httpClientDeque = new LinkedList<>();

    @Override
    public void setTimeStamp(long time) {
        lastTimeStamp = time;
    }

    @Override
    public long getTimeStamp() {
        return lastTimeStamp;
    }

    @Override
    public void release(IConnectRelease connectRelease, int leastNum) {
        LogHelper.get().i("DefaultConnectMgr", "release , httpClientDeque size=" + httpClientDeque.size());
        if (httpClientDeque.size() > 1) {
            if (leastNum < 1) {
                //全部删除、释放
                while (httpClientDeque.peekFirst() != null) {
                    httpClientDeque.pollFirst().release();
                }
                if (!httpClientDeque.isEmpty()) {
                    httpClientDeque.clear();
                }
                if (connectRelease != null) {
                    connectRelease.remove(this);
                }
            } else {
                //保留指定数量（暂时全部保存1个）
                ConnectClient saveClient = httpClientDeque.pollLast();
                while (httpClientDeque.peekFirst() != null) {
                    httpClientDeque.pollFirst().release();
                }
                if (!httpClientDeque.isEmpty()) {
                    httpClientDeque.clear();
                }
                //将选中的客户端填充队列，并更新时间
                if (saveClient != null) {
                    lastTimeStamp = saveClient.getRequestTime();
                    httpClientDeque.add(saveClient);
                }
            }
        } else if (httpClientDeque.size() == 1) {
            if (httpClientDeque.peekFirst() != null) {
                httpClientDeque.pollFirst().release();
            }
            if (connectRelease != null) {
                connectRelease.remove(this);
            }
        }
    }

    @Override
    public void put(ConnectClient client) {
        LogHelper.get().i("DefaultConnectMgr", "put , client hash=" + client.hashCode());
        if (client != null) {
            lastTimeStamp = client.getRequestTime();
            httpClientDeque.addLast(client);
        }
    }
}
