package com.walker.aop_optimize_plugin.plugins.antishake

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AntiShakePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByType(AppExtension::class.java)
        android?.apply {
            println("AntiShakePlugin is start")
            this.registerTransform(AntiShakeTransform(AntiShakeConfig()))
        }
    }
}