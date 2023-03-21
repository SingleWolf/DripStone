package com.walker.demo.vcard

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.efficiency.activityresult.ActivityResultHelper
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.R
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.fragment_demo_vacard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class VCardTestFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_vcard_fragment"
        const val TAG = "VCardTestFragment"
        fun instance() = VCardTestFragment()
    }

    private val mainScope by lazy { MainScope() }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {
        layoutShow.visibility = View.GONE

        tvTest.setOnClickListener {
            onVCardTest()
        }

        tvScan.setOnClickListener {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).explainReasonBeforeRequest()
                .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        "二维码扫描需要以下权限",
                        "允许",
                        "拒绝"
                    )
                })
                .request(
                    RequestCallback { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            onScanParse()
                        }
                    }
                )
        }
    }

    private fun onScanParse() {
        ActivityResultHelper.init(activity)
            .startActivityForResult(CaptureActivity::class.java) { resultCode, data ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringExtra(Constant.CODED_CONTENT)
                    LogHelper.get().i(TAG, "onScanParse data= $result", true)
                    result?.also {
                        if (VCardUtils.isValid(it)) {
                            parseVCardData(it)
                        } else {
                            ToastUtils.showCenterLong("不是VCrd格式")
                        }
                    } ?: also {
                        ToastUtils.showCenterLong("识别失败")
                    }
                }
            }
    }

    private fun onVCardTest() {
        val testStr = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "FN;CHARSET=UTF-8:李四\n" +
                "TEL;TYPE=CELL:13811111222\n" +
                "TEL;TYPE=WORK:021 00000888\n" +
                "ORG;CHARSET=UTF-8:中国工商银行\n" +
                "TITLE:经理三级\n" +
                "ADR;TYPE=WORK;UTF-8:上海市虹口区曲阳路888号\n" +
                "EMAIL;TYPE=internet:aaaa@sdc.icbc.com.cn\n" +
                "END:VCARD"
        parseVCardData(testStr)
    }

    private fun parseVCardData(dataStr: String) {
        mainScope.launch {
            flow {
                val vcard = VCardUtils.parseData<VCardEntity>(dataStr)
                emit(vcard)
            }.flowOn(Dispatchers.IO).collect { showInfo(it) }
        }
    }

    private fun showInfo(vcard: VCardEntity?) {
        vcard?.apply {
            layoutShow.visibility = View.VISIBLE

            tvName.text = this.name
            tvTitle.text = "职位：${this.title}"
            tvTell.text = this.tell
            tvPhone.text = this.phone
            tvEmail.text = this.email
            tvAddress.text = this.email
            tvOrganization.text = this.organization
        } ?: let {
            layoutShow.visibility = View.GONE
        }

    }

    override fun getLayoutId() = R.layout.fragment_demo_vacard
}