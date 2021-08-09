package com.walker.common.media.image.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.walker.common.R
import com.walker.common.media.image.IImageLoad
import com.walker.common.media.image.ImageConfig
import com.walker.common.media.image.OnDownloadListener
import com.walker.common.media.image.OnImageLoadListener
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author Walker
 *
 * @Date   2020-08-13 20:06
 *
 * @Summary 基于Glide封装图片加载功能
 */
class GlideLoadMgr : IImageLoad {

    companion object {
        val urlMap = ConcurrentHashMap<ImageView, String>()
    }

    private val defaultImageConfig: ImageConfig by lazy {
        ImageConfig()?.apply {
            placeholder = R.drawable.empty
            errorImage = R.drawable.error
            isCircle = false
        }
    }

    override fun loadUrl(view: ImageView, url: String, config: ImageConfig?) {
        loadUrl(view, url, config, null)
    }

    override fun loadUrl(
        view: ImageView,
        url: String,
        config: ImageConfig?,
        loadListener: OnImageLoadListener?
    ) {
        loadImage(view, url, config)
        urlMap[view] = url
        loadListener?.let {
            ProgressManager.addListener(url, it)
        }
    }

    override fun loadFile(view: ImageView, path: String, config: ImageConfig?) {
        loadImage(view, path, config)
    }

    override fun loadRes(view: ImageView, resId: Int, config: ImageConfig?) {
        loadImage(view, resId, config)
    }

    override fun downloadFile(context: Context, url: String, width: Int, height: Int): File? {
        var data: File? = null
        try {
            val target: FutureTarget<File> = Glide.with(context)
                .load(url)
                .downloadOnly(width, height)
            data = target.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    override fun downloadFile(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        downloadListener: OnDownloadListener<File>
    ) {
        val simpleTarget = if (width == 0 || height == 0) {
            object : SimpleTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    downloadListener.onResult(resource)
                }

            }
        } else {
            object : SimpleTarget<File>(width, height) {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    downloadListener.onResult(resource)
                }

            }
        }
        try {
            Glide.with(context)
                .load(url)
                .downloadOnly(simpleTarget)
        } catch (e: Exception) {
            e.printStackTrace()
            downloadListener.onError(e.toString())
        }
    }

    override fun downloadBitmap(context: Context, url: String, width: Int, height: Int): Bitmap? {
        var data: Bitmap? = null
        try {
            data = Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.fitCenterTransform())
                .into(width, height).get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    override fun downloadBitmap(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        downloadListener: OnDownloadListener<Bitmap>
    ) {
        val simpleTarget = if (width == 0 || height == 0) {
            object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    downloadListener.onResult(resource)
                }

            }
        } else {
            object : SimpleTarget<Bitmap>(width, height) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    downloadListener.onResult(resource)
                }

            }
        }
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.fitCenterTransform())
                .into(simpleTarget)
        } catch (e: Exception) {
            e.printStackTrace()
            downloadListener.onError(e.toString())
        }
    }

    fun loadImage(view: ImageView, obj: Any, imageConfig: ImageConfig?) {
        val glideRequest: RequestBuilder<Drawable> = Glide.with(view).asDrawable().load(obj)
        val config = imageConfig ?: defaultImageConfig
        config.apply {
            if (placeholder != 0) {
                glideRequest.placeholder(placeholder)
            }
            if (errorImage != 0) {
                glideRequest.error(errorImage)
            }
            if (isCircle) {
                glideRequest.transform(CircleTransformation())
            }
        }
        glideRequest.transition(DrawableTransitionOptions.withCrossFade())
            .into(GlideImageViewTarget(view))
    }

    private inner class GlideImageViewTarget internal constructor(view: ImageView) :
        DrawableImageViewTarget(view) {

        override fun onLoadStarted(placeholder: Drawable?) {
            super.onLoadStarted(placeholder)
        }

        fun getUrl(): String? {
            view ?: return ""
            return urlMap[view]
        }

        override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
            val url = getUrl()
            url?.let {
                val onProgressListener = ProgressManager.getProgressListener(it)
                onProgressListener?.run {
                    onProgress(true, 100, 0, 0)
                    ProgressManager.removeListener(it)
                }
            }
            super.onLoadFailed(errorDrawable)
        }

        override fun onResourceReady(
            @NonNull resource: Drawable,
            @Nullable transition: Transition<in Drawable>?
        ) {
            val url = getUrl()
            url?.let {
                val onProgressListener = ProgressManager.getProgressListener(it)
                onProgressListener?.run {
                    onProgress(true, 100, 0, 0)
                    ProgressManager.removeListener(it)
                }
            }
            super.onResourceReady(resource, transition)
        }
    }
}