package com.walker.dripstone.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.walker.core.log.LogHelper
import com.walker.dripstone.links.LinkHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.get().d("SplashActivity", "onCreate", true)
        if (intent.data == null) {
            // 解决部分机型按Home键退出桌面再次点击应用图标会重新启动欢迎页面问题
            if (!isTaskRoot) {
                finish()
                return
            }
        }

        transactLinks()
        Intent(this, MainActivity::class.java).let {
            startActivity(it)
        }
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
}