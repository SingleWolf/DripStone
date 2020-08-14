package com.walker.common.media.image

import android.widget.ImageView
import com.walker.common.media.image.glide.GlideLoadMgr

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:56
 *
 * @Summary 图片加载管理类
 */
object ImageLoadHelper : IImageLoad {

    private val imageLoader: IImageLoad = GlideLoadMgr()

    override fun loadUrl(view: ImageView, url: String, config: ImageConfig?) {
        imageLoader?.loadUrl(view, url, config)
    }

    override fun loadUrl(
        view: ImageView,
        url: String,
        config: ImageConfig?,
        loadListener: OnImageLoadListener?
    ) {
        imageLoader?.loadUrl(view, url, config, loadListener)
    }


    override fun loadFile(view: ImageView, path: String, config: ImageConfig?) {
        imageLoader?.loadFile(view, path, config)
    }

    override fun loadRes(view: ImageView, resId: Int, config: ImageConfig?) {
        imageLoader?.loadRes(view, resId, config)
    }

}