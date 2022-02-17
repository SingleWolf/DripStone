package com.walker.platform.share;

import static com.walker.common.share.SharePlatform.MEDIA_WEIXIN;
import static com.walker.common.share.SharePlatform.MEDIA_WEIXIN_CIRCLE;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.walker.common.share.SharePlatform;
import com.walker.core.log.LogHelper;
import com.walker.platform.R;

import java.io.File;


/**
 * Author  : walker
 * Date    : 2022/2/15  3:39 下午
 * Email   : feitianwumu@163.com
 * Summary : 分享工具类
 */
public class ShareUtils {
    public static SHARE_MEDIA convert(int platform) {
        SHARE_MEDIA share_media = SHARE_MEDIA.WEIXIN;
        if (platform == MEDIA_WEIXIN_CIRCLE) {
            share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        return share_media;
    }

    public static int convert(SHARE_MEDIA platform) {
        int share_media = MEDIA_WEIXIN;
        if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
            share_media = MEDIA_WEIXIN_CIRCLE;
        }
        return share_media;
    }

    public static String getInstallTip(int platform) {
        String tip;
        if (platform == SharePlatform.MEDIA_WEIXIN_CIRCLE || platform == SharePlatform.MEDIA_WEIXIN) {
            tip = "未安装微信客户端";
        } else {
            tip = "未安装客户端";
        }
        return tip;
    }

    public static <I> UMImage getUMImage(Activity activity, I image) {
        UMImage umImage = null;
        try {
            if (image instanceof String) {
                umImage = new UMImage(activity, (String) image);
            } else if (image instanceof Integer) {
                umImage = new UMImage(activity, (Integer) image);
            } else if (image instanceof Bitmap) {
                umImage = new UMImage(activity, (Bitmap) image);
            } else if (image instanceof File) {
                umImage = new UMImage(activity, (File) image);
            } else if (image instanceof byte[]) {
                umImage = new UMImage(activity, (byte[]) image);
            }
        } catch (Throwable throwable) {
            LogHelper.get().e("getUMImage", throwable.getMessage());
        } finally {
            if (umImage == null) {
                umImage = new UMImage(activity, R.mipmap.ic_share_default_image);
            }
        }
        return umImage;
    }
}
