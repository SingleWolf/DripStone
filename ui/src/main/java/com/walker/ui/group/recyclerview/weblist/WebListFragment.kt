package com.walker.ui.group.recyclerview.weblist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R
import kotlinx.android.synthetic.main.fragment_ui_weblist.*

/**
 * @Author Walker
 *
 * @Date   2023-04-19 10:08
 *
 * @Summary 展示recyclerView吸顶效果
 */
class WebListFragment : BaseFragment() {
    companion object {
        const val KEY_ID = "key_ui_web_list_fragment"

        fun instance() = WebListFragment()
    }

    var webDataList: MutableList<String> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initData()
    }

    private fun initData() {
        for (i in 1..10){
            webDataList.add("$i")
        }
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        rvWeb?.run {
            layoutManager = LinearLayoutManager(this@WebListFragment.context)
            adapter = WebListAdapter(webDataList)
        }
    }

    override fun getLayoutId() = R.layout.fragment_ui_weblist
}