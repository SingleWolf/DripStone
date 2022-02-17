package com.walker.common.share;

import android.app.Activity;
import android.content.Context;

import java.util.List;

/**
 * @Author Walker
 * @Date 2020-08-20 13:41
 * @Summary 分享行为接口
 */
public interface IShareAction {

    void init(Context context, List<IShareConfig> platformConfigs);

    void release(Context context);

    <I, T> void shareText(Activity activity, ShareOption<I, T> option, OnShareListener listener);

    <I, T> void shareImage(Activity activity, ShareOption<I, T> option, OnShareListener listener);

    <I, T> void shareWeb(Activity activity, ShareOption<I, T> option, OnShareListener listener);
}
