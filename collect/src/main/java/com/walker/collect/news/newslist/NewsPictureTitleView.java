package com.walker.collect.news.newslist;

import android.content.Context;
import android.view.View;

import com.walker.common.router.IWebviewRouter;
import com.walker.common.view.picturetitleview.PictureTitleView;
import com.walker.core.router.RouterLoader;

public class NewsPictureTitleView extends PictureTitleView {
    public NewsPictureTitleView(Context context) {
        super(context);
    }

    @Override
    protected void onRootClick(View view) {
        super.onRootClick(view);
        IWebviewRouter webviewRouter = RouterLoader.load(IWebviewRouter.class);
        if (webviewRouter != null) {
            webviewRouter.startActivity(view.getContext(), getViewModel().title, getViewModel().jumpUri);
        }
    }
}
