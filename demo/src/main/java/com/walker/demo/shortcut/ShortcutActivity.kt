package com.walker.demo.shortcut

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.walker.common.activity.ShowActivity
import com.walker.common.isUrl
import com.walker.common.router.IWebviewRouter
import com.walker.core.base.mvc.BaseActivity
import com.walker.core.log.LogHelper
import com.walker.core.router.RouterLoader
import com.walker.core.ui.loadsir.LoadingCallback
import com.walker.core.util.ToastUtils
import com.walker.demo.R

/**
 * Author  : walker
 * Date    : 2021/8/19  2:26 下午
 * Email   : feitianwumu@163.com
 * Summary : 快捷方式页面分发
 */
class ShortcutActivity : BaseActivity() {

    override fun init(savedInstanceState: Bundle?) {
        transactData(intent)
    }

    override fun getContentViewId() = R.layout.activity_short_cut

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        transactData(intent)
    }

    private fun transactData(intent: Intent?) {
        intent?.getStringExtra("data")?.apply {
            LogHelper.get().d("ShortcutHelper", this)
            if (this.isUrl()) {
                val webviewRouter = RouterLoader.load(IWebviewRouter::class.java)
                webviewRouter?.startActivity(
                    this@ShortcutActivity,
                    "",
                    this
                )
            } else {
                ShowActivity.start(this@ShortcutActivity, "transactData", this, null)
            }
            finish()
        }
    }

}