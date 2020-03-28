package com.walker.collect.news.newslist;

import android.content.Context;
import android.view.View;

import com.walker.common.view.picturetitleview.PictureTitleView;
import com.walker.webview.WebviewActivity;

public class NewsPictureTitleView extends PictureTitleView {
    public NewsPictureTitleView(Context context) {
        super(context);
    }

    @Override
    protected void onRootClick(View view) {
        super.onRootClick(view);
        WebviewActivity.start(view.getContext(),getViewModel().title,getViewModel().jumpUri);
    }
}
