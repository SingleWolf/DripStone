package com.walker.ui.group.colorlayout

import android.os.Bundle
import android.view.View
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R

class ColorLayoutFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_ui_color_layout_fragment"

        fun instance() = ColorLayoutFragment()
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {

    }

    override fun getLayoutId() = R.layout.fragment_ui_color_layout
}