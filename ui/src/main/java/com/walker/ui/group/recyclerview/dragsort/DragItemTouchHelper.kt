package com.walker.ui.group.recyclerview.dragsort

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class DragItemTouchHelper : ItemTouchHelper.Callback {
    var dataList: MutableList<DragData>? = null
    var adapter: DragAdapter? = null

    constructor(dataList: MutableList<DragData>, adapter: DragAdapter) : super() {
        this.dataList = dataList
        this.adapter = adapter
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val isDrag = isDragByDataConfig(viewHolder.bindingAdapterPosition)
        var dragFlags = 0
        var swipeFlags = 0
        if (isDrag) {
            //拖拽时支持的方向
            dragFlags = ItemTouchHelper.UP.or(ItemTouchHelper.DOWN).or(ItemTouchHelper.LEFT)
                .or(ItemTouchHelper.RIGHT)
            //拖拽时支持的方向
            swipeFlags = ItemTouchHelper.LEFT
        }
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        recyclerView.parent.requestDisallowInterceptTouchEvent(true)
        //得到当拖拽的viewHolder的Position
        val fromPosition = viewHolder.bindingAdapterPosition
        //拿到当前拖拽到的item的viewHolder
        val toPosition = target.bindingAdapterPosition
        transactDragAction(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        dataList?.get(position)?.apply {
            dataList?.remove(this)
        }
        adapter?.notifyItemRemoved(position)
    }

    fun transactDragAction(fromPosition: Int, toPosition: Int) {
        val isDrag = isDragByDataConfig(toPosition)
        if (!isDrag) {
            return
        }
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dataList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dataList, i, i - 1)
            }
        }
        adapter?.notifyItemMoved(fromPosition, toPosition);
        Log.d(
            "DragItemTouchHelper",
            " fromPosition = $fromPosition toPosition$toPosition"
        )
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    private fun isDragByDataConfig(bindingAdapterPosition: Int): Boolean {
        var isDrag = true
        dataList?.apply {
            if (this.size > bindingAdapterPosition) {
                isDrag = this[bindingAdapterPosition].isDrag
            }
        }
        return isDrag
    }

}