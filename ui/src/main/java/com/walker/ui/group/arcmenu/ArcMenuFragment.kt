package com.walker.ui.group.arcmenu

import android.os.Bundle
import android.view.View
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.util.ToastUtils
import com.walker.ui.R
import kotlinx.android.synthetic.main.fragment_ui_arc_menu.*

class ArcMenuFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_ui_arc_menu_fragment"

        fun instance() = ArcMenuFragment()
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        arcMenu.setOnMenuItemClickListener { view, pos ->
            ToastUtils.showCenter("pos=$pos tag=${view.tag}")
        }

        flexibleMenu.setOnMenuItemClickListener { view, pos ->
            ToastUtils.showCenter("pos=$pos tag=${view.tag}")
        }
    }

    override fun getLayoutId() = R.layout.fragment_ui_arc_menu
}