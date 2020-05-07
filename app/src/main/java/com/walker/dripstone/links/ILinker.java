package com.walker.dripstone.links;

import android.content.Context;

/**
 * @Author Walker
 * @Date 2020-04-20 21:02
 * @Summary app链接定义接口
 */
interface ILinker {

    String getKeyHost();

    boolean isEnable();

    void onTransact(Context context);

    void setConfig(String uri);
}
