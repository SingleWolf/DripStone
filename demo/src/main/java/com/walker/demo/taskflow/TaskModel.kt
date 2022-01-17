package com.walker.demo.taskflow

import androidx.lifecycle.MutableLiveData

class TaskModel private constructor() {
    companion object {
        @JvmStatic
        val instance: TaskModel by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TaskModel()
        }
    }

    val taskState by lazy {
        MutableLiveData<Int>()
    }

}