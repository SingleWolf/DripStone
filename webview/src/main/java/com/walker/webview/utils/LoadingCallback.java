package com.walker.webview.utils;

import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.walker.webview.R;

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.webview_layout_loading;
    }

    @Override
    public boolean getSuccessVisible() {
        return super.getSuccessVisible();
    }

    @Override
    protected boolean onReloadEvent(Context context, View view) {
        return true;
    }
}
