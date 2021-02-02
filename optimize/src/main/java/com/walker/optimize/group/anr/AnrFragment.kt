package com.walker.optimize.group.anr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.optimize.R
import com.walker.optimize.databinding.FragmentOptimizeAnrBinding

class AnrFragment : Fragment() {
    private lateinit var mBinding: FragmentOptimizeAnrBinding

    companion object {
        const val KEY_ID = "key_optimize_anr"
        const val ANR_TRACE_FILE = "/data/anr/traces.txt"
        fun instance(): Fragment {
            return AnrFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_optimize_anr,
            container,
            false
        )
        mBinding.clickHandler = ClickHandler()
        return mBinding.root
    }


    class ClickHandler {
        fun onWatchDog(v: View) {
            ANRWatchDog.getInstance().addANRListener(ANRWatchDog.ANRListener { stackTraceInfo ->
                LogHelper.get().e("onWatchDog", stackTraceInfo)

            })
            ANRWatchDog.getInstance().start()
        }

        fun onFileObserver(v: View) {
            ANRFileObserver(ANR_TRACE_FILE).startWatching()
        }

        fun onMockANR(v: View) {
            Thread.sleep(10000)
            ToastUtils.showCenterLong("onMockANR")
        }
    }
}