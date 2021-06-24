package com.walker.dripstone.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.walker.common.activity.ImagePreviewActivity
import com.walker.common.media.image.ImageConfig
import com.walker.common.media.image.ImageLoadHelper
import com.walker.common.media.photo.PhotoCallback
import com.walker.common.media.photo.PhotoConfig
import com.walker.common.media.photo.PhotoData
import com.walker.common.media.photo.PhotoGetterHelper
import com.walker.core.log.LogHelper
import com.walker.core.store.sp.SPHelper
import com.walker.core.util.ImageUtils
import com.walker.core.util.ToastUtils
import com.walker.dripstone.R
import com.walker.dripstone.databinding.FragmentSettingBinding

class AccountFragment : Fragment() {
    private lateinit var mBinding: FragmentSettingBinding
    private var backgroundFilePath: String? = null
    private var accountFilePath: String? = null

    companion object {
        const val FILE_PATH_BACKGROUND = "file_path_background"
        const val FILE_PATH_ACCOUNT = "file_path_account"
    }

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

        backgroundFilePath = SPHelper.get().getString(FILE_PATH_BACKGROUND, null)
        accountFilePath = SPHelper.get().getString(FILE_PATH_ACCOUNT, null)
        backgroundFilePath?.apply {
            showImage(mBinding.ivBackground, false, this!!)
        }
        accountFilePath?.apply {
            showImage(mBinding.ivAccount, true, this!!)
        }

        mBinding.ivBackground.setOnClickListener {
            backgroundFilePath?.let {
                ImagePreviewActivity.start(requireContext(), it)
            }
        }
        mBinding.ivBackground.setOnLongClickListener {
            var config = PhotoConfig()
            config.isCutCrop = true
            config.isCamera = true
            PhotoGetterHelper.get()
                .onAlbum(this@AccountFragment, config, object : PhotoCallback<PhotoData> {

                    override fun onSuccess(result: MutableList<PhotoData>?) {
                        result?.let {
                            val data = it[0]
                            backgroundFilePath = data.filePath
                            backgroundFilePath?.apply {
                                SPHelper.get().setString(FILE_PATH_BACKGROUND, this!!)
                                showImage(mBinding.ivBackground, false, this!!)
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
            true
        }

        mBinding.ivAccount.setOnClickListener {
            accountFilePath?.let {
                ImagePreviewActivity.start(requireContext(), it)
            }
        }
        mBinding.ivAccount.setOnLongClickListener {
            var config = PhotoConfig()
            config.isCutCrop = true
            config.isCamera = true
            PhotoGetterHelper.get()
                .onAlbum(this@AccountFragment, config, object : PhotoCallback<PhotoData> {

                    override fun onSuccess(result: MutableList<PhotoData>?) {
                        result?.let {
                            val data = it[0]
                            accountFilePath = data.filePath
                            accountFilePath?.apply {
                                SPHelper.get().setString(FILE_PATH_ACCOUNT, this!!)
                                showImage(mBinding.ivAccount, true, this!!)
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
            true
        }

        return mBinding.root
    }

    fun showImage(imageView: ImageView, isCircle: Boolean, filePath: String) {
        LogHelper.get().i("showImage","filePath=$filePath")
        val loadConfig = ImageConfig()
        loadConfig.isCircle = isCircle
        ImageLoadHelper.loadFile(imageView, filePath, loadConfig)
    }
}