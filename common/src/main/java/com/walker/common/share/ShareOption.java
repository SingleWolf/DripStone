package com.walker.common.share;

/**
 * @Author Walker
 * @Date 2020-08-19 13:38
 * @Summary 分享配置
 */
public class ShareOption<I, T> {
    /**
     * 目标平台
     */
    private int platform;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 链接地址
     */
    private String url;
    /**
     * 图片
     */
    private I image;
    /**
     * 缩略图
     */
    private T thumb;

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(@SharePlatform.ShareMedia int platform) {
        this.platform = platform;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public I getImage() {
        return image;
    }

    public void setImage(I image) {
        this.image = image;
    }

    public T getThumb() {
        return thumb;
    }

    public void setThumb(T thumb) {
        this.thumb = thumb;
    }
}
