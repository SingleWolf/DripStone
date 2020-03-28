package com.walker.collect.news.newslist;

import com.walker.core.base.mvvm.customview.BaseCustomViewModel;
import com.walker.core.base.mvvm.viewmodel.MvvmBaseViewModel;

public class NewsListViewModel extends MvvmBaseViewModel<NewsListModel, BaseCustomViewModel> {
    public NewsListViewModel init(String channelId) {
        model = new NewsListModel(channelId);
        model.register(this);
        model.getCachedDataAndLoad();
        return this;
    }

    public void tryToLoadNextPage() {
        model.loadNexPage();
    }
}
