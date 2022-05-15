package com.walker.buildsrc.optimize

import com.android.build.gradle.AppExtension
import com.walker.buildsrc.optimize.thread.ThreadTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

 class OptimizePlugin implements Plugin<Project> {
    void apply(Project project) {
        System.out.println("==OptimizePlugin gradle plugin==")

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new ThreadTransform())
    }
}