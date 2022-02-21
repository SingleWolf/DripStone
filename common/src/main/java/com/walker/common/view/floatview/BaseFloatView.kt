package com.walker.common.view.floatview

import android.content.Context

/**
 * Author  : walker
 * Date    : 2022/2/18  3:26 下午
 * Email   : feitianwumu@163.com
 * Summary : 悬浮框基类
 */
abstract class BaseFloatView(context: Context) : IShowFloat {

    var floatAdapter: IFloatAdapter? = null

    fun <T> setAdapter(adapter: FloatViewAdapter<T>) {
        floatAdapter = adapter
    }

}