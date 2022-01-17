package com.walker.demo.taskflow

import android.os.Bundle
import android.view.View
import com.walker.core.base.mvc.BaseFragment
import com.walker.demo.R

class TaskWebFragment : BaseFragment() {
    companion object {
        fun getInstance(url: String): BaseFragment {
            return TaskWebFragment()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        TaskModel.instance.taskState.postValue(1)
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
    }

    override fun getLayoutId()= R.layout.layout_loading


}