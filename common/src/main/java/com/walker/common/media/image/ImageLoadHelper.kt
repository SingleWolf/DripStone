package com.walker.common.media.image

import android.view.View
import com.walker.common.media.image.glide.GlideMgr

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:56
 *
 * @Summary 图片加载管理类
 */
object ImageLoadHelper : IImageLoad {

    private val imageLoader: IImageLoad=GlideMgr()

    override fun loadUrl(view: View, url: String, config: ImageConfig?) {
        imageLoader?.loadUrl(view,url,config)
    }

    override fun loadFile(view: View, path: String, config: ImageConfig?) {
        imageLoader?.loadFile(view,path,config)
    }

    override fun loadRes(view: View, resId: Int, config: ImageConfig?) {
        imageLoader?.loadRes(view,resId,config)
    }

}