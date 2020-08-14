package com.walker.common.media.image

import androidx.annotation.DrawableRes

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:53
 *
 * @Summary 图片参数
 */
data class ImageConfig(@DrawableRes val placeholder: Int, @DrawableRes val errorImage: Int, val isCircle: Boolean)