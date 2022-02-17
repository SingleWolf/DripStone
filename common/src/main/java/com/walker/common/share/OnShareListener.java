package com.walker.common.share;

/**
 * @Author Walker
 * @Date 2020-08-19 11:08
 * @Summary 分享监听
 */
public interface OnShareListener {
    void onStart();

    void onResult();

    void onError(Throwable throwable);

    void onCancel();
}
