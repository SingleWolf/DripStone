package com.walker.demo.window

import android.os.Bundle
import android.view.View
import com.walker.core.base.mvc.BaseFragment
import com.walker.demo.R
import com.walker.common.feedback.FeedbackHelper
import kotlinx.android.synthetic.main.fragment_demo_prepage_bitmap.*

class PrePageBitmapFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_prepage_bitmap_fragment"
        const val TAG = "PrePageBitmapFragment"
        fun instance() = PrePageBitmapFragment()
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        tvAction.setOnClickListener {
            genPrePageBitmapAndShow()
        }
    }

    override fun getLayoutId() = R.layout.fragment_demo_prepage_bitmap

    private fun genPrePageBitmapAndShow() {
//        FloatingWindowManager.genBitmapFromPreActivityView(requireActivity())?.apply {
//            ivShow.setImageBitmap(this)
//        }

        FeedbackHelper.getPrePageImage(requireActivity())?.apply {
            ivShow.setImageBitmap(this)
        }
    }
}