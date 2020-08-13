package com.walker.common.media.photo;

import android.app.Activity;

import androidx.fragment.app.Fragment;

/**
 * @Author Walker
 * @Date 2020-05-19 15:36
 * @Summary 图片获取接口
 */
public interface IPhotoGetter {
    void onCamera(Activity context, PhotoConfig config, PhotoCallback callback);

    void onCamera(Fragment context, PhotoConfig config, PhotoCallback callback);

    void onAlbum(Activity context,PhotoConfig config, PhotoCallback callback);

    void onAlbum(Fragment context,PhotoConfig config, PhotoCallback callback);
}
