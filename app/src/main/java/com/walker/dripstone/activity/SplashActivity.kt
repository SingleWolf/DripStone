package com.walker.dripstone.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.core.log.LogHelper
import com.walker.dripstone.R
import com.walker.dripstone.links.LinkHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.get().d("SplashActivity", "onCreate", true)
        setContentView(R.layout.activity_splash)
        if (intent.data == null) {
            // 解决部分机型按Home键退出桌面再次点击应用图标会重新启动欢迎页面问题
            if (!isTaskRoot) {
                finish()
                return
            }
        }

        PermissionX.init(this)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).explainReasonBeforeRequest()
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "应用运行需要以下权限",
                    "允许",
                    "拒绝"
                )
            })
            .request(
                RequestCallback { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        startApp()
                    } else {
                        finish()
                    }
                }
            )
    }

    private fun transactLinks() {
        if (TextUtils.equals(Intent.ACTION_VIEW, intent.action)) {
            val uri = intent.data
            uri?.let {
                LinkHelper.getInstance().setLinker(uri.toString())
            }
        }
    }

    override fun onStop() {
        super.onStop()
        LogHelper.get().d("SplashActivity", "onStop", true)
        finish()
    }

    private fun startApp() {
        transactLinks()
        startActivity(Intent(this, MainActivity::class.java))
    }
}