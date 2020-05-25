package com.walker.common.media.photo;

import android.content.Context;

/**
 * @Author Walker
 * @Date 2020-05-19 15:36
 * @Summary 图片获取接口
 */
public interface IPhotoGetter {
    void onCamera(Context context,PhotoConfig config, PhotoCallback callback);

    void onAlbum(Context context,PhotoConfig config, PhotoCallback callback);
}
