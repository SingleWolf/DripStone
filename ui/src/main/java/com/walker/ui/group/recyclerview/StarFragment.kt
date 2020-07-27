package com.walker.ui.group.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R
import kotlinx.android.synthetic.main.fragment_ui_star.*

/**
 * @Author Walker
 *
 * @Date   2020-07-27 22:08
 *
 * @Summary 展示recyclerView吸顶效果
 */
class StarFragment : BaseFragment() {
    companion object {
        const val KEY_ID = "key_ui_star_fragment"

        fun instance() = StarFragment()
    }

    var starList: MutableList<Star> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initData()
    }

    private fun initData() {
        //水果
        starList.add(Star("香蕉", 1))
        starList.add(Star("苹果", 1))
        starList.add(Star("橘子", 1))
        starList.add(Star("水蜜桃", 1))
        starList.add(Star("猕猴桃", 1))
        starList.add(Star("葡萄", 1))
        starList.add(Star("西瓜", 1))
        starList.add(Star("圣女果", 1))
        starList.add(Star("草莓", 1))
        starList.add(Star("菠萝", 1))
        starList.add(Star("火龙果", 1))
        starList.add(Star("甘蔗", 1))
        starList.add(Star("木瓜", 1))
        starList.add(Star("牛油果", 1))
        starList.add(Star("车厘子", 1))
        starList.add(Star("山竹", 1))
        starList.add(Star("释迦摩尼果", 1))
        starList.add(Star("百香果", 1))
        starList.add(Star("香梨", 1))
        starList.add(Star("哈密瓜", 1))
        //蔬菜
        starList.add(Star("萝卜", 2))
        starList.add(Star("白菜", 2))
        starList.add(Star("冬瓜", 2))
        starList.add(Star("藕", 2))
        starList.add(Star("空心菜", 2))
        starList.add(Star("芹菜", 2))
        starList.add(Star("西兰花", 2))
        starList.add(Star("生菜", 2))
        starList.add(Star("西葫芦", 2))
        starList.add(Star("毛豆", 2))
        starList.add(Star("黄豆芽", 2))
        starList.add(Star("莴苣", 2))
        starList.add(Star("山药", 2))
        starList.add(Star("油麦菜", 2))
        starList.add(Star("土豆", 2))
        starList.add(Star("胡萝卜", 2))
        starList.add(Star("刀豆", 2))
        starList.add(Star("马蹄", 2))
        starList.add(Star("蚕豆", 2))
        starList.add(Star("鸡毛菜", 2))
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        rvStar?.run {
            layoutManager = LinearLayoutManager(this@StarFragment.context)
            addItemDecoration(StarDecoration(this@StarFragment.context))
            adapter = StarAdapter(starList)
        }
    }

    override fun getLayoutId() = R.layout.fragment_ui_star
}