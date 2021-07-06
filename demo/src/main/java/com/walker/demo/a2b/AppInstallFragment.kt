package com.walker.demo.a2b

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.walker.common.BaseApplication
import com.walker.core.base.mvc.BaseFragment
import com.walker.demo.R

/**
 * 安装app
 */
class AppInstallFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_app_install_fragment"

        fun instance() = AppInstallFragment()
    }

    private lateinit var tvClickFromAssets: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val request = OneTimeWorkRequest.Builder(AppCopyWorker::class.java).build()
        WorkManager.getInstance(BaseApplication.context!!).enqueue(request)
    }

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        tvClickFromAssets = baseView.findViewById(R.id.tvFromAssets)
        tvClickFromAssets.setOnClickListener { onInstallFromAssets() }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_demo_app_install
    }

    private fun onInstallFromAssets() {
        val helper =
            InstallHelper(requireActivity(), AppCopyWorker.appLoadPath)
        helper.install()
    }
}