package com.walker.ui.group.goodfish

import android.os.Bundle
import android.view.View
import com.walker.core.base.mvc.BaseFragment
import com.walker.ui.R

class GoodFishFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_ui_good_fish_fragment"

        fun instance() = GoodFishFragment()
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
    }

    override fun getLayoutId() = R.layout.fragment_ui_good_fish
}