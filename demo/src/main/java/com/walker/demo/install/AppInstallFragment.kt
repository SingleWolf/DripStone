package com.walker.demo.install

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.walker.common.BaseApplication
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.efficiency.activityresult.ActivityResultHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 安装app
 */
class AppInstallFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_app_install_fragment"

        fun instance() = AppInstallFragment()
    }

    private lateinit var tvClickFromAssets: TextView
    private lateinit var tvSelectApk: TextView
    private lateinit var tvApkFilePath: TextView
    private lateinit var tvSilentInstall: TextView
    private lateinit var tvAutoInstall: TextView
    private lateinit var tvForwardAccessibility: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val request = OneTimeWorkRequest.Builder(AppCopyWorker::class.java).build()
        WorkManager.getInstance(BaseApplication.context!!).enqueue(request)
    }

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        tvClickFromAssets = baseView.findViewById(R.id.tvFromAssets)
        tvSelectApk = baseView.findViewById(R.id.tvSelectApk)
        tvApkFilePath = baseView.findViewById(R.id.tvApkFilePath)
        tvSilentInstall = baseView.findViewById(R.id.tvSilentInstall)
        tvAutoInstall = baseView.findViewById(R.id.tvAutoInstall)
        tvForwardAccessibility = baseView.findViewById(R.id.tvForwardAccessibility)

        tvClickFromAssets.setOnClickListener { onInstallFromAssets() }
        tvSelectApk.setOnClickListener { onSelectApk() }
        tvSilentInstall.setOnClickListener { onSilentInstall() }
        tvAutoInstall.setOnClickListener { onAutoInstall() }
        tvForwardAccessibility.setOnClickListener { onForwardAccessibility() }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_demo_app_install
    }

    private fun onInstallFromAssets() {
         InstallHelper.get().onInstall(requireActivity(), AppCopyWorker.appLoadPath)
    }

    private fun onSelectApk() {
        ActivityResultHelper.init(requireActivity()).startActivityForResult(
            FileExplorerActivity::class.java
        ) { resultCode, data ->
            if (resultCode == RESULT_OK) {
                data?.let {
                    val apkPath = it.getStringExtra("apk_path")
                    tvApkFilePath.text = apkPath
                }
            }
        }
    }

    private fun onSilentInstall() {
        if (!InstallHelper.get().isRoot) {
            ToastUtils.showCenter("没有ROOT权限，无法完成操作")
            return
        }
        val apkPath = tvApkFilePath.text.toString()
        if (TextUtils.isEmpty(apkPath)) {
            ToastUtils.showCenter("请选择安装包")
        } else {
            tvSilentInstall.text = "安装中..."
            lifecycleScope.launch(Dispatchers.Main) {
                val isSuc = withContext(Dispatchers.IO) {
                    InstallHelper.get().onSilentInstall(apkPath)
                }
                tvSilentInstall.text = "静默安装"
                if (isSuc) {
                    ToastUtils.showCenter("安装成功")
                } else {
                    ToastUtils.showCenter("安装失败")
                }

            }
        }
    }

    private fun onAutoInstall() {
        val apkPath = tvApkFilePath.text.toString()
        if (TextUtils.isEmpty(apkPath)) {
            ToastUtils.showCenter("请选择安装包")
        } else {
            InstallHelper.get().onInstall(requireActivity(), apkPath)
        }
    }

    private fun onForwardAccessibility() {
        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            startActivity(this)
        }
    }
}