package com.walker.dripstone.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.common.activity.ImagePreviewActivity
import com.walker.common.media.image.ImageConfig
import com.walker.common.media.image.ImageLoadHelper
import com.walker.common.media.photo.PhotoCallback
import com.walker.common.media.photo.PhotoConfig
import com.walker.common.media.photo.PhotoData
import com.walker.common.media.photo.PhotoGetterHelper
import com.walker.core.util.ImageUtils
import com.walker.core.util.ToastUtils
import com.walker.dripstone.R
import com.walker.dripstone.databinding.FragmentSettingBinding

class AccountFragment : Fragment() {
    private lateinit var mBinding: FragmentSettingBinding
    private var imageFilePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )
        ImageLoadHelper.loadRes(mBinding.ivTest, R.drawable.error, null)
        mBinding.ivAccount.setOnClickListener {
            var config = PhotoConfig()
            config.isCutCrop = true

            PhotoGetterHelper.get()
                .onCamera(this@AccountFragment, config, object : PhotoCallback<PhotoData> {

                    override fun onSuccess(result: MutableList<PhotoData>?) {
                        result?.let {
                            val data = it[0]
                            val bitmap = ImageUtils.getBitmap(data.filePath)
                            bitmap?.run {
                                mBinding.ivAccount.setImageBitmap(this)
                            }
                        }
                    }

                    override fun onError(msg: String?) {
                        ToastUtils.show(msg!!)
                    }

                    override fun onCancel(msg: String?) {
                        ToastUtils.show(msg!!)
                    }

                })
        }

        mBinding.ivTest.setOnClickListener {
           imageFilePath?.let {
               ImagePreviewActivity.start(context!!,it)
           }
        }
        mBinding.ivTest.setOnLongClickListener {
            var config = PhotoConfig()
            config.isCutCrop = false
            PhotoGetterHelper.get()
                .onAlbum(this@AccountFragment, config, object : PhotoCallback<PhotoData> {

                    override fun onSuccess(result: MutableList<PhotoData>?) {
                        result?.let {
                            val data = it[0]
                            val loadConfig = ImageConfig()
                            loadConfig.isCircle = true
                            imageFilePath=data.filePath
                            ImageLoadHelper.loadFile(mBinding.ivTest, data.filePath, loadConfig)
                        }
                    }

                    override fun onError(msg: String?) {
                        ToastUtils.show(msg!!)
                    }

                    override fun onCancel(msg: String?) {
                        ToastUtils.show(msg!!)
                    }

                })
            true
        }
        return mBinding.root
    }
}