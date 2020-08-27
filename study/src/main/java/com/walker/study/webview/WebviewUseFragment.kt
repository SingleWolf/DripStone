package com.walker.study.webview

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import com.walker.common.router.IWebviewRouter
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.router.RouterLoader
import com.walker.study.R
import kotlinx.android.synthetic.main.fragment_webview_use.*

class WebviewUseFragment : BaseFragment(), OnClickListener {

    companion object {
        const val KEY_ID = "key_study_webview_use_fragment"

        fun instance(): Fragment {
            return WebviewUseFragment()
        }
    }

    private val webviewImpl: IWebviewRouter? by lazy { RouterLoader.load(IWebviewRouter::class.java) }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        tvLocalDemo.setOnClickListener(this)
        tvWebPage.setOnClickListener(this)
    }

    override fun getLayoutId() = R.layout.fragment_webview_use

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvLocalDemo -> webviewImpl?.startActivity(
                activity!!, "demo",
                "file:///android_asset/demo.html"
            )
            R.id.tvWebPage -> webviewImpl?.startActivity(
                activity!!, "百度一下",
                "https://www.baidu.com"
            )

        }
    }
}