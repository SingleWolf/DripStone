package com.walker.common.media.image

import android.widget.ImageView
import androidx.annotation.DrawableRes

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:39
 *
 * @Summary 定义图片加载行为
 */
interface IImageLoad {
    fun loadUrl(view: ImageView, url: String, config: ImageConfig?)
    fun loadUrl(view: ImageView, url: String, config: ImageConfig?,loadListener: OnImageLoadListener?)
    fun loadFile(view: ImageView, path: String, config: ImageConfig?)
    fun loadRes(view: ImageView, @DrawableRes resId: Int, config: ImageConfig?)
}