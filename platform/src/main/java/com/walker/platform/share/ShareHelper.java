package com.walker.platform.share;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.walker.common.share.IShareAction;
import com.walker.common.share.IShareConfig;
import com.walker.common.share.OnShareListener;
import com.walker.common.share.ShareOption;
import com.walker.core.log.LogHelper;
import com.walker.platform.constant.PlatformConstant;

import java.util.List;


/**
 * Author  : walker
 * Date    : 2022/2/15  3:39 下午
 * Email   : feitianwumu@163.com
 * Summary : 分享辅助类
 */
public final class ShareHelper implements IShareAction {

    private static final String TAG = "ShareHelper";

    @Override
    public void init(Context context, List<IShareConfig> platformConfigs) {
        UMConfigure.init(context, PlatformConstant.UMENG_APP_KEY, "DripStone", UMConfigure.DEVICE_TYPE_PHONE, PlatformConstant.UMENG_APP_SECRET);

        for (IShareConfig shareConfig : platformConfigs) {
            if (shareConfig != null) {
                shareConfig.onInit(context);
            }
        }
    }

    @Override
    public void release(Context context) {
        UMShareAPI.get(context).release();
    }

    static class ShareListener implements UMShareListener {
        OnShareListener listener;

        ShareListener(OnShareListener lis) {
            listener = lis;
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.i(TAG, "share -> onStart : " + share_media != null ? share_media.getName() : "");
            if (listener != null) {
                listener.onStart();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.i(TAG, "share -> onResult : " + share_media != null ? share_media.getName() : "");
            if (listener != null) {
                listener.onResult();
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.i(TAG, "share -> onError : " + share_media != null ? share_media.getName() : "");
            if (listener != null) {
                listener.onError(throwable);
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.i(TAG, "share -> onCancel : " + share_media != null ? share_media.getName() : "");
            if (listener != null) {
                listener.onCancel();
            }
        }
    }

    @Override
    public <I, T> void shareText(Activity activity, ShareOption<I, T> option, OnShareListener listener) {
        if (!checkOrTip(activity, option)) {
            return;
        }

        String content = option.getDescription();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        new ShareAction(activity)
                .setPlatform(ShareUtils.convert(option.getPlatform()))
                .withText(content)
                .setCallback(new ShareListener(listener))
                .share();
    }

    @Override
    public <I, T> void shareImage(Activity activity, ShareOption<I, T> option, OnShareListener listener) {
        if (!checkOrTip(activity, option)) {
            return;
        }
        if (option.getImage() == null) {
            return;
        }
        UMImage image = ShareUtils.getUMImage(activity, option.getImage());
        if (option.getThumb() != null) {
            UMImage thumb = ShareUtils.getUMImage(activity, option.getThumb());
            image.setThumb(thumb);
        }
        new ShareAction(activity)
                .setPlatform(ShareUtils.convert(option.getPlatform()))
                .withMedia(image)
                .setCallback(new ShareListener(listener))
                .share();
    }


    @Override
    public <I, T> void shareWeb(Activity activity, ShareOption<I, T> option, OnShareListener listener) {
        if (!checkOrTip(activity, option)) {
            return;
        }

        String url = option.getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String title = option.getTitle();
        String desc = option.getDescription();
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setDescription(desc);//描述
        if (option.getThumb() != null) {
            UMImage thumb = ShareUtils.getUMImage(activity, option.getThumb());
            web.setThumb(thumb);  //缩略图
        }

        new ShareAction(activity)
                .setPlatform(ShareUtils.convert(option.getPlatform()))//传入平台
                .withMedia(web)
                .setCallback(new ShareListener(listener))//回调监听器
                .share();
    }

    private boolean isInstall(Activity activity, int platform) {
        boolean result = false;
        try {
            result = UMShareAPI.get(activity.getApplicationContext()).isInstall(activity, ShareUtils.convert(platform));
        } catch (Exception e) {
            LogHelper.get().e(TAG, e.toString(), true);
        } finally {
            return result;
        }
    }

    private <I, T> boolean checkOrTip(Activity activity, ShareOption<I, T> option) {
        if (option == null) {
            return false;
        }
        int platform = option.getPlatform();
        if (!isInstall(activity, platform)) {
            Toast.makeText(activity, ShareUtils.getInstallTip(platform), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
