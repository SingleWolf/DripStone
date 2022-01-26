package com.walker.demo.largebitmap

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.walker.common.media.image.ImageConfig
import com.walker.common.media.image.ImageLoadHelper
import com.walker.common.media.photo.PhotoCallback
import com.walker.common.media.photo.PhotoConfig
import com.walker.common.media.photo.PhotoData
import com.walker.common.media.photo.PhotoGetterHelper
import com.walker.core.base.mvc.BaseFragment
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.demo.R

/**
 * Author  : walker
 * Date    : 2022/1/26  9:54 上午
 * Email   : feitianwumu@163.com
 * Summary : 大图跨进程传递
 */
class LargeBitmapFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_large_bitmap_fragment"
        fun instance() = LargeBitmapFragment()
    }

    private lateinit var ivShow: ImageView
    private lateinit var tvTakePhoto: TextView
    private lateinit var tvSendBitmap1: TextView
    private lateinit var tvSendBitmap2: TextView
    private lateinit var tvSendBitmap3: TextView

    private var imageBitmap: Bitmap? = null

    override fun buildView(baseView: View, savedInstanceState: Bundle?) {
        ivShow = baseView.findViewById(R.id.ivShow)
        tvTakePhoto = baseView.findViewById(R.id.tvTakePhoto)
        tvSendBitmap1 = baseView.findViewById(R.id.tvSendBitmap1)
        tvSendBitmap2 = baseView.findViewById(R.id.tvSendBitmap2)
        tvSendBitmap3 = baseView.findViewById(R.id.tvSendBitmap3)

        tvTakePhoto.setOnClickListener {
            onTakePhoto()
        }
        tvSendBitmap1.setOnClickListener {
            sendBitmap4Common()
        }
        tvSendBitmap2.setOnClickListener {
            sendBitmap4SameProcess()
        }
        tvSendBitmap3.setOnClickListener {
            sendBitmap4MultiProcess()
        }
    }

    override fun getLayoutId() = R.layout.fragment_demo_large_bitmap


    private fun sendBitmap4MultiProcess() {
        imageBitmap?.apply {
           ShowBitmap2Activity.start(holdContext,this)
        }
    }

    private fun sendBitmap4SameProcess() {
        imageBitmap?.apply {
            ShowBitmapActivity.start(holdContext,this)
        }
    }

    private fun sendBitmap4Common() {
        imageBitmap?.apply {
            try {
                Intent(holdContext, ShowBitmapActivity::class.java).also {
                    it.putExtra("IMAGE_BITMAP", this)
                    startActivity(it)
                }
            } catch (e: Throwable) {
                ToastUtils.showCenterLong(e.toString())
            }
        }
    }

    private fun onTakePhoto() {
        var config = PhotoConfig()
        config.isCutCrop = true
        config.isCamera = true
        PhotoGetterHelper.get().onCamera(this, config, object : PhotoCallback<PhotoData> {
            override fun onSuccess(result: MutableList<PhotoData>?) {
                result?.let {
                    it[0]?.filePath?.apply {
                        imageBitmap = BitmapFactory.decodeFile(this)
                        showImage(ivShow, false, this)
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

    fun showImage(imageView: ImageView, isCircle: Boolean, filePath: String) {
        LogHelper.get().i("showImage", "filePath=$filePath")
        val loadConfig = ImageConfig()
        loadConfig.isCircle = isCircle
        ImageLoadHelper.loadFile(imageView, filePath, loadConfig)
    }
}