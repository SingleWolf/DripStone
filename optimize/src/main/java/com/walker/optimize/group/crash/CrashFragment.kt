package com.walker.optimize.group.crash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.walker.crash.CrashReport
import com.walker.optimize.R

class CrashFragment : Fragment() {

    private lateinit var tvJavaCrash: TextView
    private lateinit var tvJavaCrash_1: TextView
    private lateinit var tvJavaCrash_2: TextView

    private lateinit var tvNativeCrash: TextView

    companion object {
        const val KEY_ID = "key_optimize_crash"
        fun instance(): Fragment {
            return CrashFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_optimize_crash, container, false)
        tvJavaCrash = view.findViewById(R.id.tvJavaCrash)
        tvJavaCrash_1 = view.findViewById(R.id.tvJavaCrash_1)
        tvJavaCrash_2 = view.findViewById(R.id.tvJavaCrash_2)
        tvNativeCrash = view.findViewById(R.id.tvNativeCrash)
        tvJavaCrash.setOnClickListener { onJavaCrashTapped() }
        tvJavaCrash_1.setOnClickListener { onJavaCrashTapped_1() }
        tvJavaCrash_2.setOnClickListener { onJavaCrashTapped_2() }
        tvNativeCrash.setOnClickListener { onNativeCrashTapped() }
        return view
    }

    fun onJavaCrashTapped() {
        CrashReport.testJavaCrash()
    }

    fun onJavaCrashTapped_1() {
        CrashReport.testJavaCrash_1()
    }

    fun onJavaCrashTapped_2() {
        CrashReport.testJavaCrash_2()
    }

    fun onNativeCrashTapped() {
        CrashReport.testNativeCrash()
    }
}