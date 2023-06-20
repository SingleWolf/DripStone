package com.walker.ui.group.recyclerview.dragsort

import android.content.Context
import com.walker.ui.R
import com.walker.ui.group.recyclerview.adapter.UniversalAdapter
import com.walker.ui.group.recyclerview.adapter.ViewHolder

class DragAdapter(context: Context, datas: MutableList<DragData>, layoutId: Int) :
    UniversalAdapter<DragData>(context, datas, layoutId) {
    override fun convert(viewHolder: ViewHolder?, data: DragData) {
        viewHolder?.also {
            it.setImageResource(R.id.ivShow, data.image)
            it.setText(R.id.tvShow, data.title)
        }
    }
}