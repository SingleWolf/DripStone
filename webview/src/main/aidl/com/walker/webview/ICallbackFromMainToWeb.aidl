package com.walker.webview;

interface ICallbackFromMainToWeb {
    void onResult(int responseCode, String actionName, String response);
}
