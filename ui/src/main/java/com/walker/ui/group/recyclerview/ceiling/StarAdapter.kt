package com.walker.ui.group.recyclerview.ceiling

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.walker.ui.R

class StarAdapter(var dataList: MutableList<Star>) :
    RecyclerView.Adapter<StarAdapter.StarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ui_star, null)
        return StarViewHolder(view)
    }

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        holder.tvTitle?.text = dataList[position].name
    }

    override fun getItemCount() = dataList.size

    fun isGroupHeader(position: Int): Boolean {
        return if (position == 0) {
            true
        } else {
            val currentType = dataList[position].type
            val preType = dataList[position - 1].type
            currentType != preType
        }
    }

    fun getGroupName(position: Int): String {
        val type = dataList[position].type
        return if (type == 1) {
            "水果"
        } else {
            "蔬菜"
        }
    }

    class StarViewHolder(itemView: View) : ViewHolder(itemView) {

        var tvTitle: TextView? = null

        init {
            tvTitle = itemView.findViewById(R.id.tvStar)
        }
    }
}