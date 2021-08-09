package com.walker.common

import android.text.TextUtils

fun String.isUrl(): Boolean {
    return this.startsWith("https://") || this.startsWith("http://")
}

fun String.isEmptyOrNull(): Boolean {
    return this == null || TextUtils.isEmpty(this)
}