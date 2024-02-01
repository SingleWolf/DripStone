package com.walker.demo.httpclient.release;

import com.walker.core.log.LogHelper;

import org.apache.http.client.HttpClient;

/**
 * author: Walker
 * email: feitianwumu@163.com
 * date: 2023/7/11 14:23
 * desc: 连接客户端
 */
public class ConnectClient {
    private static final String TAG = "ConnectClient";
    private long requestTime = 0;
    private HttpClient client;

    public ConnectClient(HttpClient client, long requestTime) {
        this.requestTime = requestTime;
        this.client = client;
    }

    public void release() {
        try {
            LogHelper.get().i(TAG, "exec connectionManager shutdown and httpClient hashCode=" + client.hashCode());
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            LogHelper.get().e(TAG, "shutdown err:" + e);
        }
    }

    public long getRequestTime() {
        return requestTime;
    }
}
