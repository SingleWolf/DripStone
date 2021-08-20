package com.walker.demo.shortcut

data class ShortcutBean<T>(
    var id: String,
    var name: String,
    var icon: T,
    var extraData: String
)
