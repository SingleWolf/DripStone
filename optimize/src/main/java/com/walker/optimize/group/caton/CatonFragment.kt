package com.walker.optimize.group.caton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.optimize.R
import com.walker.optimize.databinding.FragmentOptimizeCatonBinding
import com.walker.optimize.group.caton.blockcanary.BlockCanary
import com.walker.optimize.group.caton.choreographer.ChoreographerHelper

class CatonFragment : Fragment() {
    private lateinit var mBinding: FragmentOptimizeCatonBinding

    companion object {
        const val KEY_ID = "key_optimize_caton"
        fun instance(): Fragment {
            return CatonFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_optimize_caton,
            container,
            false
        )
        mBinding.clickHandler = ClickHandler()
        return mBinding.root
    }

    class ClickHandler {
        fun onBlockCanaryTapped() {
            BlockCanary.install()
        }

        fun onChoreographerTapped() {
            ChoreographerHelper.start()
        }
    }
}