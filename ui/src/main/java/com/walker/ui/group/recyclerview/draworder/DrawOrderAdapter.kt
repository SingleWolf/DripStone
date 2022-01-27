package com.walker.ui.group.recyclerview.draworder

import android.content.Context
import android.graphics.Color
import com.walker.ui.R
import com.walker.ui.group.recyclerview.adapter.UniversalAdapter
import com.walker.ui.group.recyclerview.adapter.ViewHolder

class DrawOrderAdapter(context: Context?, datas: List<String>, layoutId: Int) :
    UniversalAdapter<String>(context, datas, layoutId) {
    override fun convert(viewHolder: ViewHolder?, data: String) {
        viewHolder?.setText(R.id.tvStar, data)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 4) {
            //Item4放大两倍
            holder.itemView.scaleX = 2f
            holder.itemView.scaleY = 2f
            holder.itemView.setBackgroundColor(Color.RED)
        } else {
            holder.itemView.scaleX = 1f
            holder.itemView.scaleY = 1f
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        }
        super.onBindViewHolder(holder, position)
    }
}