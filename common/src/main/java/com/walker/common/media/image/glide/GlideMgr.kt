package com.walker.common.media.image.glide

import android.view.View
import com.walker.common.media.image.IImageLoad
import com.walker.common.media.image.ImageConfig
/**
 * @Author Walker
 *
 * @Date   2020-08-13 20:06
 *
 * @Summary 基于Glide封装图片加载功能
 */
class GlideMgr :IImageLoad {

    override fun loadUrl(view: View, url: String, config: ImageConfig?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFile(view: View, path: String, config: ImageConfig?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadRes(view: View, resId: Int, config: ImageConfig?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}