package com.walker.demo.a2b

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.common.BaseApplication
import com.walker.core.log.LogHelper
import com.walker.core.store.sp.SPHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.R
import com.walker.demo.databinding.FragmentDemoAppA2bBinding

/**
 * 跨应用跳转
 */
class AppA2bFragment : Fragment() {

    companion object {
        const val KEY_ID = "key_demo_app_a2b_fragment"
        const val KEY_TARGET_PKG = "key_target_pkg"
        const val KEY_TARGET_CLASS = "key_target_class"
        const val KEY_DATA_KEY = "key_data_key"
        const val KEY_DATA_VALUE = "key_data_value"

        fun instance() = AppA2bFragment()
    }

    lateinit var dataBinding: FragmentDemoAppA2bBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_demo_app_a2b, container, false)
        dataBinding.tvGoto.setOnClickListener { onGotoTapped() }
        initView()
        return dataBinding.root
    }

    private fun initView() {
        val targetPkg = SPHelper.get().getString(KEY_TARGET_PKG, "")
        val targetClass = SPHelper.get().getString(KEY_TARGET_CLASS, "")
        val dataKey = SPHelper.get().getString(KEY_DATA_KEY, "")
        val dataValue = SPHelper.get().getString(KEY_DATA_VALUE, "")

        dataBinding.etPackageName.setText(targetPkg)
        dataBinding.etTargetClassName.setText(targetClass)
        dataBinding.etDataKey.setText(dataKey)
        dataBinding.etDataValue.setText(dataValue)
    }

    fun onGotoTapped() {
        val targetPackageName = dataBinding.etPackageName.text.toString().trim()
        if (TextUtils.isEmpty(targetPackageName)) {
            ToastUtils.show("请输入跳转应用包名")
            return
        }

        transactGoto(targetPackageName)

//        if (checkAppExist(targetPackageName)) {
//            transactGoto(targetPackageName)
//        } else {
//            ToastUtils.showCenterLong("目标应用尚未安装，操作无法执行")
//        }
    }

    private fun checkAppExist(packageName: String): Boolean {
//        var packageInfo: PackageInfo? = null
//        try {
//            packageInfo = context?.packageManager?.getPackageInfo(
//                packageName,
//                PackageManager.GET_GIDS
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return packageInfo != null
        return PackageUtils.isAvilible(activity, packageName)
    }

    private fun transactGoto(targetPackageName: String) {
        val targetClassName = dataBinding.etTargetClassName.text.toString().trim()
        if (TextUtils.isEmpty(targetClassName)) {
            ToastUtils.show("请输入跳转页面全名")
            return
        }

        var intent = Intent()
        val componentName = ComponentName(targetPackageName, targetClassName);
        intent.action = Intent.ACTION_MAIN;
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.component = componentName;

        val dataKey = dataBinding.etDataKey.text.toString().trim()
        val dataValue = dataBinding.etDataValue.text.toString().trim()
        if (!TextUtils.isEmpty(dataKey)) {
            intent.putExtra(dataKey, dataValue);
        }

        saveData(targetPackageName, targetClassName, dataKey, dataValue);

        try {
            requireActivity().startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
            LogHelper.get().e("transactGoto", e.toString())
            ToastUtils.showCenterLong("操作失败，请检查应用是否安装")
        }
    }

    private fun saveData(
        targetPackageName: String,
        targetClassName: String,
        dataKey: String,
        dataValue: String
    ) {
        SPHelper.get().setString(KEY_TARGET_PKG, targetPackageName)
        SPHelper.get().setString(KEY_TARGET_CLASS, targetClassName)
        SPHelper.get().setString(KEY_DATA_KEY, dataKey)
        SPHelper.get().setString(KEY_DATA_VALUE, dataValue)
    }
}