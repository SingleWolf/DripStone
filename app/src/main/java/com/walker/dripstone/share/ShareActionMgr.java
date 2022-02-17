package com.walker.dripstone.share;

import android.app.Activity;
import android.content.Context;

import com.walker.common.share.IShareAction;
import com.walker.common.share.IShareConfig;
import com.walker.common.share.OnShareListener;
import com.walker.common.share.ShareOption;
import com.walker.core.log.LogHelper;
import com.walker.platform.share.ShareHelper;

import java.util.List;

/**
 * @Author Walker
 * @Date 2020-08-20 10:06
 * @Summary 分享行为管理
 */
public class ShareActionMgr implements IShareAction {
    private static final String TAG = "ShareActionMgr";
    private static volatile ShareActionMgr sInstance;
    private IShareAction mShareAction;

    private ShareActionMgr() {
        mShareAction = new ShareHelper();
    }

    public static ShareActionMgr get() {
        if (sInstance == null) {
            synchronized (ShareActionMgr.class) {
                if (sInstance == null) {
                    sInstance = new ShareActionMgr();
                }
            }
        }
        return sInstance;
    }

    private void checkProxy() {
        if (mShareAction == null) {
            mShareAction = new ShareHelper();
        }
    }

    @Override
    public void init(Context context, List<IShareConfig> platformConfigs) {
        checkProxy();
        if (mShareAction == null) {
            LogHelper.get().e(TAG, "proxy is null");
            return;
        }
        mShareAction.init(context,platformConfigs);
    }

    @Override
    public void release(Context context) {
        checkProxy();
        if (mShareAction == null) {
            LogHelper.get().e(TAG, "proxy is null");
            return;
        }
        mShareAction.release(context);

    }

    @Override
    public <I, T> void shareText(Activity activity, ShareOption<I, T> option, OnShareListener listener) {
        checkProxy();
        if (mShareAction == null) {
            LogHelper.get().e(TAG, "proxy is null");
            return;
        }
        mShareAction.shareText(activity, option, listener);
    }

    @Override
    public <I, T> void shareImage(Activity activity, ShareOption<I, T> option, OnShareListener listener) {
        checkProxy();
        if (mShareAction == null) {
            LogHelper.get().e(TAG, "proxy is null");
            return;
        }
        mShareAction.shareImage(activity, option, listener);
    }

    @Override
    public <I, T> void shareWeb(Activity activity, ShareOption<I, T> option, OnShareListener listener) {
        checkProxy();
        if (mShareAction == null) {
            LogHelper.get().e(TAG, "proxy is null");
            return;
        }
        mShareAction.shareWeb(activity, option, listener);
    }
}
