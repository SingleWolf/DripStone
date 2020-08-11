package com.walker.webview.router;

import android.content.Context;

import com.google.auto.service.AutoService;
import com.walker.common.router.IWebviewRouter;
import com.walker.webview.WebviewActivity;

@AutoService({IWebviewRouter.class})
public class WebviewRouterImpl implements IWebviewRouter {
    @Override
    public void startActivity(Context context, String title, String url) {
        WebviewActivity.start(context, title, url);
    }
}
