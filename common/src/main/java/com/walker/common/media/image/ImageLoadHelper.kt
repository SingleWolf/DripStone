package com.walker.common.media.image

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.walker.common.media.image.glide.GlideLoadMgr
import java.io.File

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

    override fun downloadFile(context: Context, url: String, width: Int, height: Int): File? {
        return getProxy()?.downloadFile(context, url, width, height)
    }

    override fun downloadFile(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        downloadListener: OnDownloadListener<File>
    ) {
        getProxy()?.downloadFile(context, url, width, height, downloadListener)
    }

    override fun downloadBitmap(context: Context, url: String, width: Int, height: Int): Bitmap? {
        return getProxy()?.downloadBitmap(context, url, width, height)
    }

    override fun downloadBitmap(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        downloadListener: OnDownloadListener<Bitmap>
    ) {
        getProxy()?.downloadBitmap(context, url, width, height, downloadListener)
    }

}