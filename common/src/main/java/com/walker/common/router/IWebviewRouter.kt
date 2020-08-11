package com.walker.common.router

import android.content.Context

/**
 * @Author Walker
 * @Date 2020-08-11 10:42
 * @Summary Webview路由提供
 */
interface IWebviewRouter {
    fun startActivity(context: Context, url: String, title: String)
}
