package com.walker.ui.group.floatlayout

import android.os.Bundle
import android.view.View
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R

class FloatLayoutFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_ui_float_layout_fragment"

        fun instance() = FloatLayoutFragment()
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {

    }

    override fun getLayoutId() = R.layout.fragment_ui_float_layout
}