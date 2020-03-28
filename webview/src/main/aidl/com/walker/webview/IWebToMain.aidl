package com.walker.webview;

import com.walker.webview.ICallbackFromMainToWeb;

interface IWebToMain {
      void handleWebAction(String actionName, String jsonParams, in ICallbackFromMainToWeb callback);
}
