package com.walker.dripstone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.dripstone.R
import com.walker.dripstone.databinding.FragmentOthersBinding

class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentOthersBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_others,
            container,
            false
        )
        mBinding.tvTitle.text = getString(R.string.menu_home)
        return mBinding.root
    }
}