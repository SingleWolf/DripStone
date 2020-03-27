package com.walker.collect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.collect.R
import com.walker.collect.databinding.FragmentCollectEmptyBinding

class EmptyFragment : Fragment() {
    private lateinit var mBinding: FragmentCollectEmptyBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_collect_empty,
            container,
            false
        )
        return mBinding.root
    }

}