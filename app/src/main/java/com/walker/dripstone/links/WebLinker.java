package com.walker.dripstone.links;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.walker.webview.WebviewActivity;

/**
 * @Author Walker
 * @Date 2020-04-20 21:04
 * @Summary 网页链接处理
 */
public class WebLinker implements ILinker {

    public static final String KEY_HOST = "";

    private boolean isEnable = true;

    private String url;

    @Override
    public String getKeyHost() {
        return KEY_HOST;
    }

    @Override
    public boolean isEnable() {
        return isEnable;
    }

    @Override
    public void onTransact(Context context) {
        if (!TextUtils.isEmpty(url)) {
            WebviewActivity.start(context, "DeepLink", url);
        }
        isEnable = false;
    }

    @Override
    public void setConfig(String uri) {
        if (!TextUtils.isEmpty(uri)) {
            Uri data = Uri.parse(uri);
            url = data.getPath().replace("/url/", "");
        }
    }
}
