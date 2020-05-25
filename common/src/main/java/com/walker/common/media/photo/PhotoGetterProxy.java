package com.walker.common.media.photo;

import android.content.Context;

/**
 * @Author Walker
 * @Date 2020-05-19 16:08
 * @Summary 图片获取辅助类
 */
public class PhotoGetterProxy implements IPhotoGetter {

    private static PhotoGetterProxy sInstance;

    private IPhotoGetter photoGetter;

    private PhotoGetterProxy() {
        photoGetter = new PictureSelectorMrg();
    }

    public static final PhotoGetterProxy get() {
        if (sInstance == null) {
            synchronized (PhotoGetterProxy.class) {
                if (sInstance == null) {
                    sInstance = new PhotoGetterProxy();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onCamera(Context context, PhotoConfig config, PhotoCallback callback) {
        photoGetter.onCamera(context, config, callback);
    }

    @Override
    public void onAlbum(Context context, PhotoConfig config, PhotoCallback callback) {
        photoGetter.onAlbum(context, config, callback);
    }
}
