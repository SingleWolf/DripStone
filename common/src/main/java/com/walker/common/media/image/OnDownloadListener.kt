package com.walker.common.media.image

/**
 * Author  : walker
 * Date    : 2021/8/20  10:23 上午
 * Email   : feitianwumu@163.com
 * Summary : 图片下载监听器
 */
interface OnDownloadListener<T> {
    fun onResult(data: T)
    fun onError(err: String)
}