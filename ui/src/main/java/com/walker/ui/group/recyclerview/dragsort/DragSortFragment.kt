package com.walker.ui.group.recyclerview.dragsort

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R
import kotlinx.android.synthetic.main.fragment_ui_drag_sort.*

/**
 * author: Walker
 * email: feitianwumu@163.com
 * date: 2023/6/20 14:40
 * desc: 拖拽排序功能
 */
class DragSortFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_ui_drag_sort_fragment"

        fun instance() = DragSortFragment()
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
        val dataList = mutableListOf<DragData>()
        var imageId = 0
        for (i in 0 until 12) {
            if (i % 3 == 0) {
                imageId = R.color.red
            } else if (i % 3 == 1) {
                imageId = R.color.blue
            } else if (i % 3 == 2) {
                imageId = R.color.green
            }
            DragData("$i", imageId).also {
                dataList.add(it)
            }
        }

        val myLayoutManager = GridLayoutManager(holdContext, 3)
//        val myLayoutManager = LinearLayoutManager(holdContext)
        recyclerView.layoutManager = myLayoutManager
        recyclerView.isNestedScrollingEnabled = true

        val dragAdapter = DragAdapter(holdContext, dataList,R.layout.item_ui_image)
        recyclerView.adapter = dragAdapter

        val itemTouchHelper = ItemTouchHelper(DragItemTouchHelper(dataList, dragAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getLayoutId() = R.layout.fragment_ui_drag_sort
}