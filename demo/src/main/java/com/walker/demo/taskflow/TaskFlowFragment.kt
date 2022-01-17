package com.walker.demo.taskflow

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.efficiency.taskflow.TaskFlow
import com.walker.core.efficiency.taskflow.TaskFlow.FlowCallBack
import com.walker.core.efficiency.taskflow.TaskNode
import com.walker.core.util.ToastUtils
import com.walker.demo.R


/**
 * Author  : walker
 * Date    : 2022/1/12  1:55 下午
 * Email   : feitianwumu@163.com
 * Summary :  任务流
 */
class TaskFlowFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_task_flow_fragment"

        fun instance() = TaskFlowFragment()
    }

    lateinit var tvTaskOne: TextView
    lateinit var tvTaskTwo: TextView
    lateinit var tvTaskThree: TextView

    private var taskFlowTwo: TaskFlow? = null
    private var taskFlowThree: TaskFlow? = null

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        tvTaskOne = baseView.findViewById(R.id.tvTaskOne)
        tvTaskTwo = baseView.findViewById(R.id.tvTaskTwo)
        tvTaskThree = baseView.findViewById(R.id.tvTaskThree)

        tvTaskOne.setOnClickListener {
            onTaskOneTap()
        }

        tvTaskTwo.setOnClickListener {
            onTaskOneTwoTap()
        }

        tvTaskThree.setOnClickListener {
            onTaskOneThreeTap()
        }

        TaskModel.instance.taskState.observe(viewLifecycleOwner, {
            if (it == 1) {
                taskFlowTwo?.continueWork()
            }
        })
    }

    override fun getLayoutId() = R.layout.fragment_demo_task_flow

    private fun onTaskOneTap() = TaskFlow.Builder().withNode(TaskNode(1) {
        val node = it
        AlertDialog.Builder(holdContext)
            .setTitle("关卡")
            .setMessage("一连杀")
            .setPositiveButton("K.O.", null)
            .setOnDismissListener {
                node.onCompleted()
            }.create().show()
    }).withNode(TaskNode(2) {
        val node = it
        AlertDialog.Builder(holdContext)
            .setTitle("关卡")
            .setMessage("二连杀")
            .setPositiveButton("K.O.", null)
            .setOnDismissListener {
                node.onCompleted()
            }.create().show()
    }).withNode(TaskNode(3) {
        val node = it
        AlertDialog.Builder(holdContext)
            .setTitle("关卡")
            .setMessage("三连杀")
            .setPositiveButton("K.O.", null)
            .setOnDismissListener {
                node.onCompleted()
            }.create().show()
    }).setCallBack(object : FlowCallBack {
        override fun onNodeChanged(nodeId: Int) {
            ToastUtils.show("欢迎来到第${nodeId}关")
        }

        override fun onFlowFinish() {
            ToastUtils.showCenter("恭喜！通关成功")
        }

    }).create().start()

    private fun onTaskOneTwoTap() {
        taskFlowTwo = TaskFlow.Builder().withNode(TaskNode(1) {
            val node = it
            AlertDialog.Builder(holdContext)
                .setTitle("温馨提示")
                .setMessage("欢迎到来")
                .setPositiveButton("下一步,冲个浪", null)
                .setOnDismissListener {
                    node.onCompleted()
                }.create().show()
        }).withNode(TaskNode(2) {
            TaskWebActivity.start(holdContext, "https://baidu.com", "冲个浪吧")
        }).withNode(TaskNode(3) {
            ToastUtils.show("终于等到你了")
        }).create()
        taskFlowTwo?.start()
    }

    private fun onTaskOneThreeTap() {
        taskFlowThree = TaskFlow.Builder().withNode(TaskNode(1) {
            val node = it
            AlertDialog.Builder(holdContext)
                .setTitle("温馨提示")
                .setMessage("欢迎到来")
                .setPositiveButton("下一步,吃个水果", null)
                .setOnDismissListener {
                    node.onCompleted()
                }.create().show()
        }).withNode(TaskNode(2) { node ->
            val fruitArray = arrayOf("香蕉", "苹果", "猕猴桃", "火龙果")
            AlertDialog.Builder(holdContext).setTitle("请选择一个水果")
                .setSingleChoiceItems(fruitArray, 0,
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        // 第二个参数是设置默认选中哪一项-1代表默认都不选
                        taskFlowThree?.addNode(TaskNode(4) {
                            ToastUtils.show("原来你喜欢吃${fruitArray[which]}")
                            tvTaskThree.postDelayed({
                                it.onCompleted()
                            }, 1000)
                        })
                        node.onCompleted()
                    }).create().show()
        }).withNode(TaskNode(9) {
            val node = it
            AlertDialog.Builder(holdContext)
                .setTitle("温馨提示")
                .setMessage("终于等到你了，哈哈")
                .setPositiveButton("ok", null)
                .setOnDismissListener {
                    node.onCompleted()
                }.create().show()
        }).create()
        taskFlowThree?.start()
    }

}