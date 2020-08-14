package com.walker.common.media.image

import androidx.annotation.DrawableRes

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:53
 *
 * @Summary 图片参数
 */
class ImageConfig {
    @DrawableRes
    var placeholder: Int = 0
    @DrawableRes
    var errorImage: Int = 0
    var isCircle: Boolean = false
}