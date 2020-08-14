package com.walker.common.media.photo.pictureselector;

import android.app.Activity;
import android.os.Build;

import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelectionModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.walker.common.media.photo.IPhotoGetter;
import com.walker.common.media.photo.PhotoCallback;
import com.walker.common.media.photo.PhotoConfig;
import com.walker.common.media.photo.PhotoData;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Walker
 * @Date 2020-05-19 16:06
 * @Summary 基于PictureSelector封装图片获取功能
 */
public class PictureSelectorMrg implements IPhotoGetter {

    @Override
    public void onCamera(Activity context, PhotoConfig config, PhotoCallback callback) {
        openCamera(context, config, callback);
    }

    @Override
    public void onCamera(Fragment context, PhotoConfig config, PhotoCallback callback) {
        openCamera(context, config, callback);
    }

    @Override
    public void onAlbum(Activity context, PhotoConfig config, PhotoCallback callback) {
        openAlbum(context, config, callback);

    }

    @Override
    public void onAlbum(Fragment context, PhotoConfig config, PhotoCallback callback) {
        openAlbum(context, config, callback);

    }

    private void openCamera(Object context, PhotoConfig config, PhotoCallback callback) {
        if (context == null) {
            return;
        }
        PictureSelectionModel pictureSelectorModel = null;
        if (context instanceof Activity) {
            pictureSelectorModel = PictureSelector.create((Activity) context).openCamera(PictureMimeType.ofImage());
        } else if (context instanceof Fragment) {
            pictureSelectorModel = PictureSelector.create((Fragment) context).openCamera(PictureMimeType.ofImage());
        }
        if (pictureSelectorModel == null) {
            return;
        }
        if (config != null) {
            boolean isCrop = config.isCutCrop();
            pictureSelectorModel.isEnableCrop(isCrop);
        }
        pictureSelectorModel.imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        setResultCallback(callback, result);
                    }

                    @Override
                    public void onCancel() {
                        setCancelCallback(callback);
                    }
                });
    }


    private void openAlbum(Object context, PhotoConfig config, PhotoCallback callback) {
        if (context == null) {
            return;
        }
        PictureSelectionModel pictureSelectorModel = null;
        if (context instanceof Activity) {
            pictureSelectorModel = PictureSelector.create((Activity) context).openGallery(PictureMimeType.ofImage());
        } else if (context instanceof Fragment) {
            pictureSelectorModel = PictureSelector.create((Fragment) context).openGallery(PictureMimeType.ofImage());
        }
        if (pictureSelectorModel == null) {
            return;
        }
        if (config != null) {
            boolean isCrop = config.isCutCrop();
            pictureSelectorModel.isEnableCrop(isCrop);
            int maxNum = config.getMaxNum();
            pictureSelectorModel.maxSelectNum(maxNum);
            int minNum = config.getMinNum();
            pictureSelectorModel.minSelectNum(minNum);
            boolean isCamera=config.isCamera();
            pictureSelectorModel.isCamera(isCamera);
        }
        pictureSelectorModel.imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        setResultCallback(callback, result);
                    }

                    @Override
                    public void onCancel() {
                        setCancelCallback(callback);
                    }
                });
    }


    private void setResultCallback(PhotoCallback callback, List<LocalMedia> result) {
        if (callback == null) {
            return;
        }
        if (result == null) {
            callback.onError("data err");
            return;
        }
        List<PhotoData> dataList = new ArrayList<>();
        for (LocalMedia data : result) {
            PhotoData d = new PhotoData();
            if (28 < Build.VERSION.SDK_INT) {
                d.setFilePath(data.getAndroidQToPath());
            } else {
                d.setFilePath(data.getCutPath());
            }
            dataList.add(d);
        }
        callback.onSuccess(dataList);
    }

    private void setCancelCallback(PhotoCallback callback) {
        if (callback == null) {
            return;
        }
        callback.onCancel("action cancel");
    }
}
