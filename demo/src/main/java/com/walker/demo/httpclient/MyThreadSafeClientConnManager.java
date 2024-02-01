package com.walker.demo.httpclient;

import com.walker.core.log.LogHelper;

import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class MyThreadSafeClientConnManager extends ThreadSafeClientConnManager {
    public MyThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
        super(params, schreg);
    }

    @Override
    protected void finalize() throws Throwable {
        LogHelper.get().i("HttpClient", "MyThreadSafeClientConnManager -> finalize()");
        super.finalize();
    }

    @Override
    public void shutdown() {
        LogHelper.get().i("HttpClient", "MyThreadSafeClientConnManager -> shutdown()");
        super.shutdown();
    }
}
