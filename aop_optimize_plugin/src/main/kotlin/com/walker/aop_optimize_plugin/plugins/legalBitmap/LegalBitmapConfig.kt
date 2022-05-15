package com.walker.aop_optimize_plugin.plugins.legalBitmap

data class LegalBitmapConfig(
    private val monitorImageViewClass: String = "com.walker.optimize.group.oom.imageview.MonitorImageView"
) {

    val formatMonitorImageViewClass: String
        get() = monitorImageViewClass.replace(".", "/")

}