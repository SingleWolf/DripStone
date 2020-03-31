package com.walker.common.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.common.R
import com.walker.common.databinding.FragmentCommonEmptyBinding

class EmptyFragment : Fragment() {

    companion object {

        const val KEY_BUNDLE_MSG = "key_bundle_msg"

        fun instance(message: String = ""): Fragment {
            val fragment = EmptyFragment()
            val data = Bundle()
            data.putString(KEY_BUNDLE_MSG, message)
            fragment.arguments = data
            return fragment
        }
    }

    private lateinit var mBinding: FragmentCommonEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_common_empty,
            container,
            false
        )

        arguments?.run {
            val msg = getString(KEY_BUNDLE_MSG)
            msg?.let {
                if (!TextUtils.isEmpty(it)) {
                    mBinding.tvMessage.text = it
                }
            }
        }

        return mBinding.root
    }

}