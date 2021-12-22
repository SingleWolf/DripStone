package com.walker.common.location.base

interface ILocCallback {

    fun isKeep(): Boolean = false

    fun onResult(data: Map<String, String>)
}