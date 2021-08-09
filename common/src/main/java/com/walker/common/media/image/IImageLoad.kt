package com.walker.common.media.image

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import java.io.File

/**
 * @Author Walker
 *
 * @Date   2020-08-13 19:39
 *
 * @Summary 定义图片加载行为
 */
interface IImageLoad {
    fun loadUrl(view: ImageView, url: String, config: ImageConfig?)
    fun loadUrl(
        view: ImageView,
        url: String,
        config: ImageConfig?,
        loadListener: OnImageLoadListener?
    )

    fun loadFile(view: ImageView, path: String, config: ImageConfig?)
    fun loadRes(view: ImageView, @DrawableRes resId: Int, config: ImageConfig?)
    fun downloadFile(context: Context, url: String, width: Int, height: Int): File?
    fun downloadFile(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        downloadListener: OnDownloadListener<File>
    )

    fun downloadBitmap(context: Context, url: String, width: Int, height: Int): Bitmap?
    fun downloadBitmap(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        downloadListener: OnDownloadListener<Bitmap>
    )
}