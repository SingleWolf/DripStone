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
import ezvcard.Ezvcard
import ezvcard.VCard
import ezvcard.parameter.AddressType
import ezvcard.parameter.EmailType
import ezvcard.parameter.TelephoneType
import kotlinx.android.synthetic.main.fragment_demo_vacard.*


class VCardTestFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_vcard_fragment"
        const val TAG = "VCardTestFragment"
        fun instance() = VCardTestFragment()
    }

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
                        if (result.contains("BEGIN:VCARD") && result.contains("END:VCARD")) {
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
                "FN;CHARSET=UTF-8:张三\n" +
                "TEL;TYPE=CELL:13811111111\n" +
                "TEL;TYPE=WORK:021 00000000\n" +
                "ORG;CHARSET=UTF-8:中国工商银行\n" +
                "TITLE:经理一级\n" +
                "ADR;TYPE=WORK;UTF-8:上海市虹口区曲阳路888号\n" +
                "EMAIL;TYPE=internet:aaaa@sdc.icbc.com.cn\n" +
                "END:VCARD"
        parseVCardData(testStr)
    }

    private fun parseVCardData(dataStr: String) {
        try {
            val vcard = Ezvcard.parse(dataStr).first()
            vcard?.also {
                LogHelper.get().i(TAG, "$it", true)
                showInfo(it)
            }
        } catch (e: Throwable) {
            LogHelper.get().i(TAG, e.message, true)
            layoutShow.visibility = View.GONE
        }
    }

    private fun showInfo(vcard: VCard) {
        layoutShow.visibility = View.VISIBLE
        tvName.text = ""
        tvTitle.text = ""
        tvTell.text = ""
        tvPhone.text = ""
        tvEmail.text = ""
        tvAddress.text = ""
        tvOrganization.text = ""

        vcard.formattedName?.also {
            tvName.text = it.value
        }

        if (vcard.titles.isNotEmpty()) {
            tvTitle.text = "职位：${vcard.titles[0].value}"
        }

        vcard.telephoneNumbers?.apply {
            this.forEachIndexed { index, telephone ->
                if (telephone.parameters.type.toLowerCase() == TelephoneType.WORK.value) {
                    tvTell.text = telephone.text
                } else if (telephone.parameters.type.toLowerCase() == TelephoneType.CELL.value) {
                    tvPhone.text = telephone.text
                }
            }
        }

        vcard.addresses?.apply {
            this.forEachIndexed { index, address ->
                if (address.parameters.type.toLowerCase() == AddressType.WORK.value) {
                    tvAddress.text = address.poBox
                }
            }
        }

        vcard.emails?.apply {
            this.forEachIndexed { index, email ->
                if (email.parameters.type.toLowerCase() == EmailType.INTERNET.value) {
                    tvEmail.text = email.value
                }
            }
        }

        vcard.organization?.apply {
            if (this.values.isNotEmpty()) {
                tvOrganization.text = this.values[0]
            }
        }

    }

    override fun getLayoutId() = R.layout.fragment_demo_vacard
}