package com.walker.common.feedback

import android.app.Activity

interface IPlayerCapacity {
    fun showPop(activity: Activity)

    fun dismissPop()

    fun transact()

    fun getCurrentFlag(): String?
}