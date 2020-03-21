package com.walker.ui.summary

import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView
import com.walker.common.view.titleview.TitleView
import com.walker.core.base.mvvm.customview.BaseCustomViewModel
import com.walker.core.base.mvvm.customview.BaseViewHolder


/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
class SummaryRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var data: ObservableList<BaseCustomViewModel>? = null

    internal fun setData(items: ObservableList<BaseCustomViewModel>) {
        data = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val titleView = TitleView(parent.context)
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

