package com.walker.common.view.banner.holder;

/**
 * @author Walker
 * @e-mail feitianwumu@163.com
 * @date on 2018/9/12
 * @summary banner布局构造器
 */

public interface HolderCreator<VH extends ViewHolder> {
    /**
     * 创建ViewHolder
     */
    VH createViewHolder();
}
