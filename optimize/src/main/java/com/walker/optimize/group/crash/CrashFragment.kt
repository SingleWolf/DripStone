package com.walker.optimize.group.crash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.walker.optimize.R

class CrashFragment : Fragment() {

    private lateinit var tvJavaCrash: TextView
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
        val view = inflater.inflate(R.layout.fragment_optimize_crash, container,false)
        tvJavaCrash = view.findViewById(R.id.tvJavaCrash)
        tvNativeCrash = view.findViewById(R.id.tvNativeCrash)
        tvJavaCrash.setOnClickListener { onJavaCrashTapped() }
        tvNativeCrash.setOnClickListener { onNativeCrashTapped() }
        return view
    }

    fun onJavaCrashTapped() {
        //CrashReport.testJavaCrash()
    }

    fun onNativeCrashTapped() {
       // CrashReport.testNativeCrash()
    }
}