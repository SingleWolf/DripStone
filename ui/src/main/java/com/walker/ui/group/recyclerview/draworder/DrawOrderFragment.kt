package com.walker.ui.group.recyclerview.draworder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R

/**
 * Author  : walker
 * Date    : 2022/1/27  9:40 上午
 * Email   : feitianwumu@163.com
 * Summary : 自定义绘制顺序
 */
class DrawOrderFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_ui_draw_order_fragment"

        fun instance() = DrawOrderFragment()
    }

    private var starList: MutableList<String> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var switchBtn: Switch
    private lateinit var adapter: DrawOrderAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initData()
    }

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        recyclerView = baseView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(holdContext)
        adapter = DrawOrderAdapter(holdContext, starList, R.layout.item_ui_star)
        recyclerView.adapter = adapter

        switchBtn = baseView.findViewById(R.id.switchBtn)
        switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //自定义绘制顺序
                recyclerView.setChildDrawingOrderCallback { childCount, i ->
                    when {
                        i < 4 -> {
                            i
                        }
                        i < childCount - 1 -> {
                            i + 1
                        }
                        else -> {
                            //最后绘制Item4
                            4
                        }
                    }
                }
            } else {
                recyclerView.setChildDrawingOrderCallback(null)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun getLayoutId() = R.layout.fragment_ui_draw_order

    private fun initData() {
        //水果
        starList.add("香蕉")
        starList.add("苹果")
        starList.add("橘子")
        starList.add("水蜜桃")
        starList.add("猕猴桃")
        starList.add("葡萄")
        starList.add("西瓜")
        starList.add("圣女果")
        starList.add("草莓")
        starList.add("菠萝")
        starList.add("火龙果")
        starList.add("甘蔗")
        starList.add("木瓜")
        starList.add("牛油果")
        starList.add("车厘子")
        starList.add("山竹")
        starList.add("释迦摩尼果")
        starList.add("百香果")
        starList.add("香梨")
        starList.add("哈密瓜")
        //蔬菜
        starList.add("萝卜")
        starList.add("白菜")
        starList.add("冬瓜")
        starList.add("藕")
        starList.add("空心菜")
        starList.add("芹菜")
        starList.add("西兰花")
        starList.add("生菜")
        starList.add("西葫芦")
        starList.add("毛豆")
        starList.add("黄豆芽")
        starList.add("莴苣")
        starList.add("山药")
        starList.add("油麦菜")
        starList.add("土豆")
        starList.add("胡萝卜")
        starList.add("刀豆")
        starList.add("马蹄")
        starList.add("蚕豆")
        starList.add("鸡毛菜")
    }

}