package com.walker.demo.vcard

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
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
                "FN;CHARSET=UTF-8:峰峰\n" +
                "N;CHARSET=UTF-8:1;2;3\n" +
                "TEL;TYPE=CELL:1\n" +
                "TEL;TYPE=WORK:1\n" +
                "TEL;TYPE=HOME:1\n" +
                "TEL;TYPE=FAX:1\n" +
                "ORG;CHARSET=UTF-8:中国工商银行股份有限公司\n" +
                "TITLE:\n" +
                "ADR;TYPE=WORK;UTF-8:办公室地址\n" +
                "EMAIL;TYPE=internet:caisy@163.com\n" +
                "ID:888804029\n" +
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
            LogHelper.get().i(TAG, this.toString())

            layoutShow.visibility = View.VISIBLE

            var showName = ""
            if (TextUtils.isEmpty(this.lastName)) {
                showName = this.userName
            } else {
                val structuredNames = this.lastName.split(";")
                var first = ""
                var second = ""
                structuredNames?.also {
                    it.forEachIndexed { index, s ->
                        if (index == 0) {
                            first = s
                        } else {
                            second = "${second}${s}"
                        }
                    }
                }
                showName = if (TextUtils.isEmpty(second)) {
                    "${first}${this.userName}"
                } else {
                    "${second}${first}"
                }
            }
            tvName.text = showName

            if (!TextUtils.isEmpty(this.title)) {
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = "职位：${this.title}"
            } else {
                tvTitle.visibility = View.GONE
            }
            tvTell.text = this.tell
            tvPhone.text = this.phone
            tvEmail.text = this.email
            tvAddress.text = this.address
            tvOrganization.text = this.organization
        } ?: let {
            layoutShow.visibility = View.GONE
        }

    }

    override fun getLayoutId() = R.layout.fragment_demo_vacard
}