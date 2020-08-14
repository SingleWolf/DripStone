package com.walker.common.media.image;

/**
 * @Author Walker
 * @Date 2020-08-14 16:50
 * @Summary 图片加载监听
 */
public interface OnImageLoadListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
