package com.walker.common.media.photo;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.walker.common.media.photo.pictureselector.PictureSelectorMrg;

/**
 * @Author Walker
 * @Date 2020-05-19 16:08
 * @Summary 图片获取辅助类
 */
public class PhotoGetterHelper implements IPhotoGetter {

    private static PhotoGetterHelper sInstance;

    private IPhotoGetter photoGetter;

    private PhotoGetterHelper() {
    }

    public static final PhotoGetterHelper get() {
        if (sInstance == null) {
            synchronized (PhotoGetterHelper.class) {
                if (sInstance == null) {
                    sInstance = new PhotoGetterHelper();
                }
            }
        }
        return sInstance;
    }

    private IPhotoGetter getProxy() {
        if (photoGetter == null) {
            photoGetter = new PictureSelectorMrg();
        }
        return photoGetter;
    }

    @Override
    public void onCamera(Activity context, PhotoConfig config, PhotoCallback callback) {
        getProxy().onCamera(context, config, callback);
    }

    @Override
    public void onCamera(Fragment context, PhotoConfig config, PhotoCallback callback) {
        getProxy().onCamera(context, config, callback);
    }

    @Override
    public void onAlbum(Activity context, PhotoConfig config, PhotoCallback callback) {
        getProxy().onAlbum(context, config, callback);
    }

    @Override
    public void onAlbum(Fragment context, PhotoConfig config, PhotoCallback callback) {
        getProxy().onAlbum(context, config, callback);
    }
}
