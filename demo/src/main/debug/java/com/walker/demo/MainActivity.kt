package com.walker.demo

import android.Manifest
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.summary.SummaryFragment
import kotlinx.android.synthetic.main.activity_demo_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_main)
        initToolbar()

        PermissionX.init(this)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
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
                        initFragment()
                    } else {
                        finish()
                    }
                }
            )
    }

    private fun initToolbar() {
        //Set Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.app_name)
        }
    }

    private fun initFragment() {
        val fragment = SummaryFragment()
        val manger = supportFragmentManager
        val transaction = manger.beginTransaction()
        transaction.add(com.walker.common.R.id.container, fragment!!, fragment!!.javaClass.name)
            .commit()
    }
}