package com.walker.demo.httpclient;

import com.walker.core.log.LogHelper;
import com.walker.demo.httpclient.release.ConnectClient;
import com.walker.demo.httpclient.release.ConnectReleaseHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RequestUtils {
    private static final String TAG = "RequestUtils";

    public static HttpClient getHttpClient() {
        HttpParams httpParams = new BasicHttpParams();
        // 设置连接超时时间
        HttpConnectionParams.setConnectionTimeout(httpParams, 500);
        // 设置读取超时时间
        HttpConnectionParams.setSoTimeout(httpParams, 2000);
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
        HttpConnectionParams.setTcpNoDelay(httpParams, true);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager connectionManager = new MyThreadSafeClientConnManager(httpParams, registry);
        return new DefaultHttpClient(connectionManager, httpParams);
    }

    public static void postRequest() {
        try {
            List<BasicNameValuePair> data = new ArrayList<BasicNameValuePair>();
            data.add(new BasicNameValuePair("name", "jack"));
            data.add(new BasicNameValuePair("age", "14"));
            data.add(new BasicNameValuePair("country", "中国"));
            HttpEntity entity = new UrlEncodedFormEntity(data, "UTF-8");

            String url = "http://www.taobao.com";
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);

            HttpClient httpClient = getHttpClient();
            ConnectReleaseHelper.get().put(url, new ConnectClient(httpClient, System.currentTimeMillis()));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            //获取字符串，指定UTF8避免乱码
            String content = EntityUtils.toString(responseEntity, "UTF-8");
            LogHelper.get().i(TAG, "postRequest successful , response 'size = " + content.length());
            consume(responseEntity);
            ConnectReleaseHelper.get().release(url);
        } catch (Exception e) {
            LogHelper.get().e(TAG, "postRequest error : " + e);
        }
    }

    private static void consume(final HttpEntity entity) throws IOException {
        if (entity == null) {
            return;
        }
        if (entity.isStreaming()) {
            InputStream instream = entity.getContent();
            if (instream != null) {
                instream.close();
            }
        }
    }
}
