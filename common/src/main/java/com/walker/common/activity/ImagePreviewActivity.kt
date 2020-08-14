package com.walker.common.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.walker.common.R
import com.walker.common.view.PhotoView
import com.walker.core.util.ImageUtils

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var photoView: PhotoView

    private lateinit var filePath: String

    companion object {
        const val KEY_PARAM_FILE_PATH = "key_param_file_path"

        fun start(
            context: Context,
            filePath: String
        ) {
            if (TextUtils.isEmpty(filePath)) {
                return
            }

            Intent(context, ImagePreviewActivity::class.java).let {
                it.putExtra(KEY_PARAM_FILE_PATH, filePath)
                if (context is Activity) {
                    context.startActivity(it)
                } else {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(it)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_common_image_preview)
        photoView = findViewById(R.id.photoView)
        filePath = intent.getStringExtra(KEY_PARAM_FILE_PATH)
        filePath?.let {
            val imageBitmap = ImageUtils.getBitmap(it)
            imageBitmap?.run {
                photoView.setBitmap(this)
            }
        }
    }
}