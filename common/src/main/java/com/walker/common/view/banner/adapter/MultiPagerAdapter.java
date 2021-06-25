package com.walker.common.view.banner.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.walker.common.view.banner.holder.HolderCreator;
import com.walker.common.view.banner.holder.ViewHolder;
import com.walker.common.view.banner.view.MultiBanner;

import java.util.List;

/**
 * @author Walker
 * @date on 2018/5/17 0017 下午 15:06
 * @email feitianwumu@163.com
 * @desc 页面适配器
 */

public class MultiPagerAdapter<T> extends PagerAdapter {
    private List<T> list;
    private MultiBanner viewPager;
    private HolderCreator holderCreator;
    private boolean isCanLoop;

    public void setCanLoop(boolean canLoop) {
        isCanLoop = canLoop;
    }

    public MultiPagerAdapter(List<T> list, MultiBanner viewPager, HolderCreator holderCreator) {
        this.list = list;
        this.viewPager = viewPager;
        this.holderCreator = holderCreator;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = getView(position, container);
        container.addView(view);
        return view;
    }


    //  根据图片URL创建对应的ImageView并添加到集合
    private View getView(final int position, ViewGroup container) {
        ViewHolder holder = holderCreator.createViewHolder();
        if (holder == null) {
            throw new RuntimeException("can not return a null holder");
        }
        View view = null;
        if (list != null && list.size() > 0) {
            if (isCanLoop) {
                int size = list.size();
                if (list.size() > 1) {
                    size = list.size() - 2;
                }
                if (position == 0) {
                    view = holder.createView(container.getContext(), list.size() - 1);
                    holder.onBind(container.getContext(), list.get(0), list.size() - 1, size);
                } else if (position == list.size() - 1) {
                    view = holder.createView(container.getContext(), 0);
                    holder.onBind(container.getContext(), list.get(list.size() - 1), 0, size);
                } else {
                    view = holder.createView(container.getContext(), position - 1);
                    holder.onBind(container.getContext(), list.get(position), position - 1, size);
                }
            } else {
                view = holder.createView(container.getContext(), position);
                holder.onBind(container.getContext(), list.get(position), position, list.size());
            }
        }

        if (view != null)
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.imageClick(position);
                }
            });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);

    }
}
