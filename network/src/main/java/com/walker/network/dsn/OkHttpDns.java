package com.walker.network.dsn;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.walker.core.log.LogHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

public class OkHttpDns implements Dns {
    HttpDnsService httpDns;//httpDns 解析服务
    private static OkHttpDns instance = null;

    private OkHttpDns(Context context) {
        this.httpDns = HttpDns.getService(context, "174447");
    }

    public static OkHttpDns getInstance(Context context) {
        if (instance == null) {
            instance = new OkHttpDns(context);
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        //通过异步解析接口获取ip
        String ip = httpDns.getIpByHostAsync(hostname);
        if (!TextUtils.isEmpty(ip)) {
            //如果ip不为null，直接使用该ip进行网络请求
            List<InetAddress> inetAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            LogHelper.get().d("OkHttpDns", "AliDsn.SYSTEM.lookup:" + inetAddresses, true);
            return inetAddresses;
        }
        //如果返回null，走系统DNS服务解析域名
        List<InetAddress> inetAddresses = Dns.SYSTEM.lookup(hostname);
        LogHelper.get().d("OkHttpDns", "Dns.SYSTEM.lookup:" + inetAddresses, true);
        return inetAddresses;
    }
}
