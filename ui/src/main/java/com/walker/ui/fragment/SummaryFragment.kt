package com.walker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.walker.core.ui.loadsir.EmptyCallback
import com.walker.core.ui.loadsir.ErrorCallback
import com.walker.core.ui.loadsir.LoadingCallback
import com.walker.ui.R
import com.walker.ui.databinding.FragmentSummaryBinding


class SummaryFragment:Fragment() {

    private lateinit var viewBinding:FragmentSummaryBinding
//    private lateinit var loadService:LoadService<Any>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_summary,container,false)
//        val loadSir = LoadSir.Builder()
//            .addCallback(LoadingCallback())
//            .addCallback(EmptyCallback())
//            .addCallback(ErrorCallback())
//            .build()
//        loadService = loadSir.register(viewBinding?.layoutContent) {
//            // 重新加载逻辑
//        }
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        loadService?.showCallback(EmptyCallback::class.java)
    }
}