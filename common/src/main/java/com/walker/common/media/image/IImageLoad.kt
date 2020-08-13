package com.walker.common.media.image

import android.view.View
import androidx.annotation.DrawableRes

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:39
 *
 * @Summary 定义图片加载行为
 */
interface IImageLoad {
    fun loadUrl(view: View, url: String, config: ImageConfig?)
    fun loadFile(view: View, path: String, config: ImageConfig?)
    fun loadRes(view: View, @DrawableRes resId: Int, config: ImageConfig?)
}