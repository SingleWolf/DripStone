package com.walker.common.view.floatview

import android.view.View

interface IFloatAdapter {
    fun getLayoutId():Int
    fun attachView(view: View)
    fun notifyDataChanged()
}