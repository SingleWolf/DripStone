package com.walker.collect.cook.cooklist;

import android.view.ViewGroup;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.walker.collect.news.newslist.NewsPictureTitleView;
import com.walker.common.view.picturetitleview.PictureTitleViewViewModel;
import com.walker.common.view.titleview.TitleView;
import com.walker.common.view.titleview.TitleViewViewModel;
import com.walker.common.view.waterfallview.WaterFallView;
import com.walker.core.base.mvvm.customview.BaseCustomViewModel;
import com.walker.core.base.mvvm.customview.BaseViewHolder;

public class CookListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private ObservableList<BaseCustomViewModel> mItems;

    void setData(ObservableList<BaseCustomViewModel> items) {
        mItems = items;
        notifyDataSetChanged();
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
        WaterFallView waterFallView=new WaterFallView(parent.getContext());
        return new BaseViewHolder(waterFallView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }
}
