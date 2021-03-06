package com.walker.webview.remotewebview.commanddispatcher;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.walker.webview.ICallbackFromMainToWeb;
import com.walker.webview.IWebToMain;
import com.walker.webview.command.CommandsManager;
import com.walker.webview.command.ResultBack;
import com.walker.webview.remotewebview.BaseWebView;
import com.walker.webview.utils.MainLooper;
import com.walker.webview.utils.WebConstants;

import java.util.Map;

/**
 * WebView所有请求分发
 * <p>
 * 规则：
 * <p>
 * 1、先处理UI依赖
 * 2、再判断是否是跨进程通信，跨进程通信需要通过AIDL方式分发数据
 */
public class CommandDispatcher {

    private static CommandDispatcher instance;
    private Gson gson = new Gson();

    // 实现跨进程通信的接口
    protected IWebToMain webAidlInterface;

    public static CommandDispatcher getInstance() {
        if (instance == null) {
            synchronized (CommandDispatcher.class) {
                if (instance == null) {
                    instance = new CommandDispatcher();
                }
            }
        }
        return instance;
    }

    public void initAidlConnect(final Context context) {
        if (webAidlInterface != null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("AIDL", "Begin to connect with main process");
                webAidlInterface = IWebToMain.Stub.asInterface(MainProcessConnector.getInstance(context).getIWebAidlInterface());
                Log.i("AIDL", "Connect success with main process");
            }
        }).start();
    }

    public void exec(Context context, String cmd, String params, String callbackName, final WebView webView) {
        Log.i("CommandDispatcher", "command: " + cmd + " callbackName:" + callbackName + " params: " + params);
        try {
            if (CommandsManager.getInstance().isWebviewProcessCommand(cmd)) {
                Map mapParams = gson.fromJson(params, Map.class);
                CommandsManager.getInstance().execWebviewProcessCommand(context, cmd, mapParams, new ResultBack() {
                    @Override
                    public void onResult(int status, String actionName, Object result) {
                        handleCallback(status, actionName, callbackName, gson.toJson(result), webView);
                    }
                });
            } else {
                if (webAidlInterface != null) {
                    webAidlInterface.handleWebAction(cmd, params, new ICallbackFromMainToWeb.Stub() {
                        @Override
                        public void onResult(int responseCode, String actionName, String response) {
                            handleCallback(responseCode, actionName, callbackName, response, webView);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.e("CommandDispatcher", "Command exec error!!!!", e);
        }
    }

    private void handleCallback(final int responseCode, final String actionName, String callbackName, final String response,
                                final WebView webView) {
        Log.d("CommandDispatcher", String.format("Callback result: action= %s, result= %s", actionName, response));
        MainLooper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Map params = new Gson().fromJson(response, Map.class);
//                if (params.get(WebConstants.NATIVE2WEB_CALLBACK) != null && !TextUtils.isEmpty(params.get(WebConstants.NATIVE2WEB_CALLBACK).toString())) {
                    if (webView instanceof BaseWebView) {
                        ((BaseWebView) webView).handleCallback(callbackName, response);
                    }
//                }
            }
        });
    }
}
