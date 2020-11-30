package com.walker.common.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.walker.common.R
import com.walker.core.util.ImageUtils
import kotlinx.android.synthetic.main.activity_common_image_preview.*

class ImagePreviewActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_image_preview)
        initToolbar()
        filePath = intent.getStringExtra(KEY_PARAM_FILE_PATH).toString()
        filePath?.let {
            val imageBitmap = ImageUtils.getBitmap(it)
            imageBitmap?.run {
                photoView.setBitmap(this)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }// case blocks for other MenuItems (if any)
        return true
    }

    private fun initToolbar() {
        //Set Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }
}