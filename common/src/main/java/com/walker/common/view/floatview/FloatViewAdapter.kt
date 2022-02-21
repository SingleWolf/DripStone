package com.walker.common.view.floatview

import android.view.View

/**
 * Author  : walker
 * Date    : 2022/2/18  2:59 下午
 * Email   : feitianwumu@163.com
 * Summary : 悬浮框适配器
 */
abstract class FloatViewAdapter<T> : IFloatAdapter {
    private var contentView: View? = null
    private var callback: OnCallback? = null

    override fun attachView(view: View) {
        contentView = view
        notifyDataChanged()
    }

    abstract fun getData(): T
    abstract fun bindView(view: View, data: T)

    override fun notifyDataChanged() {
        contentView?.apply {
            if (getData() != null) {
                bindView(this, getData())
            }
        }
    }

    fun <I> setCallback(callback: OnCallback) {
        this.callback = callback
    }

    fun getCallback() = callback

    interface OnCallback {
        fun <I> callback(value: I)
    }
}