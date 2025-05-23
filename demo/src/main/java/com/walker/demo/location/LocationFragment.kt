package com.walker.demo.location

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import com.walker.common.location.LocationHelper
import com.walker.common.location.baidu.BaiduLocation
import com.walker.common.location.base.ILocCallback
import com.walker.common.location.base.LocConstant
import com.walker.common.location.gaode.GaodeLocation
import com.walker.core.base.mvc.BaseFragment
import com.walker.demo.R
import java.io.File

class LocationFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_location_fragment"

        fun instance() = LocationFragment()
    }

    private lateinit var tvBaiduLocShow: TextView
    private lateinit var tvGaodeLocShow: TextView
    private lateinit var tvBaiduLocOnce: TextView
    private lateinit var tvBaiduLocKeep: TextView
    private lateinit var tvGaodeLocOnce: TextView
    private lateinit var tvGaodeLocKeep: TextView


    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        tvBaiduLocShow = baseView.findViewById(R.id.tvBaiduLoc)
        tvGaodeLocShow = baseView.findViewById(R.id.tvGaodeLoc)
        tvBaiduLocOnce = baseView.findViewById(R.id.tvBaiduLocOne)
        tvBaiduLocKeep = baseView.findViewById(R.id.tvBaiduLocKeep)
        tvGaodeLocOnce = baseView.findViewById(R.id.tvGaodeLocOne)
        tvGaodeLocKeep = baseView.findViewById(R.id.tvGaodeLocKeep)

        tvBaiduLocOnce.setOnClickListener {
            LocationHelper.start(BaiduLocation.KEY, object : ILocCallback {
                override fun onResult(data: Map<String, String>) {
                    if (data[LocConstant.KEY_SUC] == LocConstant.CODE_SUC) {
                        tvBaiduLocShow.text = "$data"
                    } else {
                        tvBaiduLocShow.text = "定位失败"
                    }
                }
            })
        }

        tvBaiduLocKeep.setOnClickListener {
            LocationHelper.start(BaiduLocation.KEY, object : ILocCallback {
                override fun onResult(data: Map<String, String>) {
                    if (data[LocConstant.KEY_SUC] == LocConstant.CODE_SUC) {
                        tvBaiduLocShow.text = "$data"
                    } else {
                        tvBaiduLocShow.text = "定位失败"
                    }
                }

                override fun isKeep(): Boolean {
                    return true
                }
            })
        }

        tvGaodeLocOnce.setOnClickListener {
//            LocationHelper.start(GaodeLocation.KEY, object : ILocCallback {
//                override fun onResult(data: Map<String, String>) {
//                    if (data[LocConstant.KEY_SUC] == LocConstant.CODE_SUC) {
//                        tvGaodeLocShow.text = "$data"
//                    } else {
//                        tvGaodeLocShow.text = "定位失败"
//                    }
//                }
//            })
            Intent(Intent.ACTION_SEND)?.apply {
                val filePath = "${Environment.getExternalStorageDirectory()}/zbtrace.txt"
                val uri: Uri = FileProvider.getUriForFile(
                    holdContext,
                    holdContext.packageName + ".install_file_provider",
                    File(filePath)
                )

                putExtra(Intent.EXTRA_STREAM, filePath)
                type = "*/*"
                holdContext.startActivity(Intent.createChooser(this, "分享到:"))
            }
        }

        tvGaodeLocKeep.setOnClickListener {
            LocationHelper.start(GaodeLocation.KEY, object : ILocCallback {
                override fun onResult(data: Map<String, String>) {
                    if (data[LocConstant.KEY_SUC] == LocConstant.CODE_SUC) {
                        tvGaodeLocShow.text = "$data"
                    } else {
                        tvGaodeLocShow.text = "定位失败"
                    }
                }

                override fun isKeep(): Boolean {
                    return true
                }
            })
        }
    }

    override fun getLayoutId() = R.layout.fragment_demo_location
}