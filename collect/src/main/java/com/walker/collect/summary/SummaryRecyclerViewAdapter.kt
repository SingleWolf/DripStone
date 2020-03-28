package com.walker.collect.summary

import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.core.base.mvvm.customview.BaseViewHolder

class SummaryRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var data: ObservableList<BaseCustomViewModel>? = null

    internal fun setData(items: ObservableList<BaseCustomViewModel>) {
        data = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val titleView = SummaryTitleView(parent.context)
        return BaseViewHolder(titleView)
    }

    override fun getItemCount(): Int {
        var itemCount = 0
        data?.let {
            itemCount = it.size
        }
        return itemCount
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        data?.let {
            holder.bind(it[position])
        }
    }
}

