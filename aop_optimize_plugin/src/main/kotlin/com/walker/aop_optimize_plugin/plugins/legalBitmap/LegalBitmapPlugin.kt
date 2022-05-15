package com.walker.aop_optimize_plugin.plugins.legalBitmap

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class LegalBitmapPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val appExtension = project.extensions.getByType(AppExtension::class.java)
        appExtension?.apply {
            println("LegalBitmapPlugin is start")
            registerTransform(LegalBitmapTransform(LegalBitmapConfig()))
        }
    }

}