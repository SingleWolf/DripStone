package com.walker.aop_optimize_plugin.plugins.thread

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ShadowThreadPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)
        android?.apply {
            println("ShadowThreadPlugin is start")
            this.registerTransform(ShadowThreadTransform(ShadowThreadConfig()))
        }
    }
}