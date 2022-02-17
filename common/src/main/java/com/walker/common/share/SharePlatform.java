package com.walker.common.share;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Walker
 * @Date 2020-08-19 11:13
 * @Summary 分享平台
 */
public class SharePlatform {
    /**
     * 微信好友
     */
    public static final int MEDIA_WEIXIN = 1;
    /**
     * 微信朋友圈
     */
    public static final int MEDIA_WEIXIN_CIRCLE = 2;

    @IntDef({MEDIA_WEIXIN, MEDIA_WEIXIN_CIRCLE})
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShareMedia {
    }
}
