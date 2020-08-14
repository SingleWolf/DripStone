package com.walker.common.media.image.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.walker.common.media.image.IImageLoad
import com.walker.common.media.image.ImageConfig
import com.walker.common.media.image.OnImageLoadListener
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

    fun loadImage(view: ImageView, obj: Any, config: ImageConfig?) {
        val glideRequest: RequestBuilder<Drawable> = Glide.with(view.context).asDrawable().load(obj)
        config?.run {
            if (placeholder != 0) {
                glideRequest.placeholder(placeholder)
            }
            if (errorImage != 0) {
                glideRequest.error(errorImage)
            }
            if (isCircle) {

            }
        }
        glideRequest.transition(withCrossFade()).into(GlideImageViewTarget(view))
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

        override fun onResourceReady(@NonNull resource: Drawable, @Nullable transition: Transition<in Drawable>?) {
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