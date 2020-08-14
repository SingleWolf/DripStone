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

    private var imageLoader: IImageLoad? = null

    fun getProxy(): IImageLoad? {
        imageLoader ?: let {
            imageLoader = GlideLoadMgr()
        }
        return imageLoader
    }

    override fun loadUrl(view: ImageView, url: String, config: ImageConfig?) {
        getProxy()?.loadUrl(view, url, config)
    }

    override fun loadUrl(
        view: ImageView,
        url: String,
        config: ImageConfig?,
        loadListener: OnImageLoadListener?
    ) {
        getProxy()?.loadUrl(view, url, config, loadListener)
    }


    override fun loadFile(view: ImageView, path: String, config: ImageConfig?) {
        getProxy()?.loadFile(view, path, config)
    }

    override fun loadRes(view: ImageView, resId: Int, config: ImageConfig?) {
        getProxy()?.loadRes(view, resId, config)
    }

}