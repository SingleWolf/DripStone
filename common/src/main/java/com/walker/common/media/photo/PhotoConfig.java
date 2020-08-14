package com.walker.common.media.photo;

/**
 * @Author Walker
 * @Date 2020-05-19 15:55
 * @Summary 图片预置参数
 */
public class PhotoConfig {
    public static final int DEFAULT_MAX_NUM = 9;
    private int maxNum = DEFAULT_MAX_NUM;
    private int minNum;
    private boolean cutCrop;
    private boolean camera;

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public boolean isCutCrop() {
        return cutCrop;
    }

    public void setCutCrop(boolean cutCrop) {
        this.cutCrop = cutCrop;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }
}
