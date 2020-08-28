package com.walker.webview.remotewebview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.walker.core.util.GsonUtils;
import com.walker.webview.remotewebview.callback.WebViewCallBack;
import com.walker.webview.remotewebview.commanddispatcher.CommandDispatcher;
import com.walker.webview.remotewebview.settings.WebviewDefaultSetting;
import com.walker.webview.remotewebview.webviewclient.WebviewClient;

import java.util.HashMap;
import java.util.Map;

public class BaseWebView extends WebView implements WebviewClient.WebviewTouch {
    private static final String TAG = "WebView";
    public static final String CONTENT_SCHEME = "file:///android_asset/";
    protected Context context;
    private WebViewCallBack webViewCallBack;
    private Map<String, String> mHeaders;

    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        this.context = context;
        WebviewDefaultSetting.getInstance().toSetting(this);
        addJavascriptInterface(this, "walkerJs");
        CommandDispatcher.getInstance().initAidlConnect(getContext());
    }

    public WebViewCallBack getWebViewCallBack() {
        return webViewCallBack;
    }

    public void registerdWebViewCallBack(WebViewCallBack webViewCallBack) {
        this.webViewCallBack = webViewCallBack;
        setWebViewClient(new WebviewClient(this, webViewCallBack, mHeaders, this));
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    @JavascriptInterface
    public void takeNativeAction(String request) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (webViewCallBack != null) {
                        Map<String, String> data = GsonUtils.fromLocalJson(request, Map.class);
                        String cmd = data.get("name");
                        String param = data.get("param");
                        String callbackName = data.get("callbackname");
                        Log.d(TAG, "=============takeNativeAction: " + request);
                        CommandDispatcher.getInstance().exec(context, cmd, param, callbackName, BaseWebView.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadUrl(String url) {
        if (mHeaders == null) {
            super.loadUrl(url);
        } else {
            super.loadUrl(url, mHeaders);
        }
        Log.d(TAG, "DWebView load url: " + url);
        resetAllStateInternal(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        Log.d(TAG, "DWebView load url: " + url);
        resetAllStateInternal(url);
    }

    public void handleCallback(String callbackName, String response) {
        Log.d(TAG, String.format("handleCallback(response=%s)", callbackName, response));
        if (!TextUtils.isEmpty(response)) {
            String trigger = "javascript:" + "webviewjs.callback('" + callbackName + "','" + response + "')";
            evaluateJavascript(trigger, null);
        }
    }

    public void loadJS(String cmd, Object param) {
        String trigger = "javascript:" + cmd + "(" + new Gson().toJson(param) + ")";
        evaluateJavascript(trigger, null);
    }

    public void dispatchEvent(String name) {
        Map<String, String> param = new HashMap<>(1);
        param.put("name", name);
        loadJS("webviewjs.dispatchEvent", param);
    }

    private boolean mTouchByUser;

    @Override
    public boolean isTouchByUser() {
        return mTouchByUser;
    }

    private void resetAllStateInternal(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return;
        }
        resetAllState();
    }

    // 加载url时重置touch状态
    protected void resetAllState() {
        mTouchByUser = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchByUser = true;
                break;
        }
        return super.onTouchEvent(event);
    }
}