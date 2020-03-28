package com.walker.collect.news.newslist;

import android.view.ViewGroup;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.walker.common.view.picturetitleview.PictureTitleViewViewModel;
import com.walker.common.view.titleview.TitleView;
import com.walker.common.view.titleview.TitleViewViewModel;
import com.walker.core.base.mvvm.customview.BaseCustomViewModel;
import com.walker.core.base.mvvm.customview.BaseViewHolder;

public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ObservableList<BaseCustomViewModel> mItems;

    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;

    void setData(ObservableList<BaseCustomViewModel> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position) instanceof PictureTitleViewViewModel){
            return VIEW_TYPE_PICTURE_TITLE;
        } else if(mItems.get(position) instanceof TitleViewViewModel){
            return VIEW_TYPE_TITLE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if(mItems != null && mItems.size() > 0) {
            return mItems.size();
        }
        return 0;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_PICTURE_TITLE) {
            NewsPictureTitleView pictureTitleView = new NewsPictureTitleView(parent.getContext());
            return new BaseViewHolder(pictureTitleView);
        } else if(viewType == VIEW_TYPE_TITLE) {
            TitleView titleView = new TitleView(parent.getContext());
            return new BaseViewHolder(titleView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }
}
