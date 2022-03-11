package com.walker.optimize.group.lancet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.walker.core.util.ToastUtils
import com.walker.optimize.R

class LancetFragment : Fragment() {

    private lateinit var tvTryCatch: TextView
    private lateinit var tvAopLogin: TextView

    companion object {
        const val KEY_ID = "key_optimize_lancet"
        fun instance(): Fragment {
            return LancetFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_optimize_lancet, container, false)
        tvTryCatch = view.findViewById(R.id.tvTryCatch)
        tvTryCatch.setOnClickListener { onTryCatchTapped() }

        tvAopLogin = view.findViewById(R.id.tvAopLogin)
        tvAopLogin.setOnClickListener { onAopLoginTapped() }

        return view
    }

    fun onTryCatchTapped() {
        LancetTest.testError("optimize")
    }

    fun onAopLoginTapped() {
        ToastUtils.showCenterLong("姓名：Walker\n年龄：23")
    }

}