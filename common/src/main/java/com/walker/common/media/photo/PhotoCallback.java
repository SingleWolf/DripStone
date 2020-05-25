package com.walker.common.media.photo;

import java.util.List;

/**
 * @Author Walker
 * @Date 2020-05-19 15:59
 * @Summary 图片回传接口
 */
public interface PhotoCallback<T extends PhotoData> {
    void onSuccess(List<T> result);

    void onCancel(String msg);
}
