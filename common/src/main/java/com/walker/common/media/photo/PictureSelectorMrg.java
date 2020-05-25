package com.walker.common.media.photo;

import android.app.Activity;
import android.content.Context;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.walker.core.util.ToastUtils;

import java.util.List;

/**
 * @Author Walker
 * @Date 2020-05-19 16:06
 * @Summary 基于PictureSelector封装图片获取功能
 */
public class PictureSelectorMrg implements IPhotoGetter {

    @Override
    public void onCamera(Context context, PhotoConfig config, PhotoCallback callback) {
        PictureSelector.create((Activity) context)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        ToastUtils.showCenter(result.size() + "");
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }

    @Override
    public void onAlbum(Context context, PhotoConfig config, PhotoCallback callback) {
        PictureSelector.create((Activity) context)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // onResult Callback
                        ToastUtils.showCenter(result.size() + "");
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                    }
                });
    }
}
