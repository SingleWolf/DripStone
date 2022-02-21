package com.walker.common.view.floatview

object FloatWindowHelper {

    private val floatViewMap = mutableMapOf<String, IShowFloat>()

    public fun <T> show(tag: String, data: T) {
        if (floatViewMap.containsKey(tag)) {

        }
    }

    public fun dismiss(tag: String) {

    }
}