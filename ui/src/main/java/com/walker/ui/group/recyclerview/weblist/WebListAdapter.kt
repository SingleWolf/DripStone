package com.walker.ui.group.recyclerview.weblist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.walker.ui.R

class WebListAdapter(var dataList: MutableList<String>) :
    RecyclerView.Adapter<WebListAdapter.StarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ui_web, null)
        return StarViewHolder(view)
    }

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        holder.webView?.loadUrl("file:///android_asset/gxb_test.html")
    }

    override fun getItemCount() = dataList.size

    class StarViewHolder(itemView: View) : ViewHolder(itemView) {

        var webView: WebView? = null

        init {
            webView = itemView.findViewById(R.id.webContent)
            webView?.apply {
                // 设置与Js交互的权限
                settings.javaScriptEnabled = true
                // 设置允许JS弹窗
                settings.javaScriptCanOpenWindowsAutomatically = true
            }

        }
    }
}