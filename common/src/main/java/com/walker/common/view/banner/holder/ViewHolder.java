package com.walker.common.view.banner.holder;

import android.content.Context;
import android.view.View;

/**
 *@author Walker
 *
 *@e-mail feitianwumu@163.com
 *
 *@date on 2018/9/12
 *
 *@summary banner布局容纳器
 *
 */
public interface ViewHolder<T> {
    View createView(Context context, int position);
   // void onBind(Context context, int position, T data);
    /**
     * @param context context
     * @param data 实体类对象
     * @param position 当前位置
     * @param size 页面个数
     */
    void onBind(Context context, T data, int position, int size);
}
