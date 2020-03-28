package com.walker.collect.news.newslist;

import android.text.TextUtils;

import com.walker.collect.api.JuheNetworkApi;
import com.walker.common.view.picturetitleview.PictureTitleViewViewModel;
import com.walker.common.view.titleview.TitleViewViewModel;
import com.walker.core.base.mvvm.customview.BaseCustomViewModel;
import com.walker.core.base.mvvm.model.MvvmBaseModel;
import com.walker.network.retrofit.observer.BaseObserver;

import java.util.ArrayList;

public class NewsListModel extends MvvmBaseModel<NewsListBean, ArrayList<BaseCustomViewModel>> {
    private static final String NEWS_KEY = "1f98e1e9e106fa40cf2f4ced41680b99";
    private String mChannelId = "";

    public NewsListModel(String channelId) {
        super(NewsListBean.class, true, "pref_key_news_" + channelId, null, 0);
        mChannelId = channelId;
    }

    @Override
    public void refresh() {
        isRefresh = true;
        load();
    }

    public void loadNexPage() {
        isRefresh = false;
        load();
    }

    @Override
    protected void load() {
        JuheNetworkApi.getService(NewsApiInterface.class).getNewsList(NEWS_KEY, mChannelId)
                .compose(JuheNetworkApi.get().applySchedulers(new BaseObserver<NewsListBean>(this, this)));
    }

    @Override
    public void onSuccess(NewsListBean newsListBean, boolean isFromCache) {
        ArrayList<BaseCustomViewModel> baseViewModels = new ArrayList<>();
        for (NewsListBean.ResultBean.DataBean source : newsListBean.getResult().getData()) {
            if (TextUtils.isEmpty(source.getThumbnail_pic_s())) {
                TitleViewViewModel viewModel = new TitleViewViewModel();
                viewModel.jumpUri = source.getUrl();
                viewModel.title = source.getTitle();
                baseViewModels.add(viewModel);
            } else {
                PictureTitleViewViewModel viewModel = new PictureTitleViewViewModel();
                viewModel.jumpUri = source.getUrl();
                viewModel.title = source.getTitle();
                viewModel.avatarUrl = source.getThumbnail_pic_s();
                baseViewModels.add(viewModel);
            }
        }
        loadSuccess(newsListBean, baseViewModels, isFromCache);
    }

    @Override
    public void onFailure(Throwable e) {
        e.printStackTrace();
        loadFail(e.getMessage());
    }
}
