package com.walker.demo.largebitmap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.walker.core.base.mvc.BaseActivity
import com.walker.demo.R
import kotlinx.coroutines.*

class ShowBitmap2Activity : BaseActivity() {

    private lateinit var ivImageShow: ImageView
    private lateinit var toolbar: Toolbar

    companion object {
        fun start(context: Context, bitmap: Bitmap) {
            Intent(context, ShowBitmap2Activity::class.java).also {
                it.putExtras(Bundle().apply {
                    putBinder("IMAGE_BINDER", object : IGetBitmapService.Stub() {
                        override fun getIntentBitmap(): Bitmap {
                            return bitmap
                        }
                    })
                })
                context.startActivity(it)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        initToolbar()
        ivImageShow = findViewById(R.id.ivShowImage)
        showImageView()
    }

    override fun getContentViewId() = R.layout.activity_demo_show_bitmap

    private fun showImageView() {
        runBlocking {
            val bitmap = withContext(Dispatchers.Default) {
                val bundle: Bundle? = intent.extras
                val getBitmapService =
                    IGetBitmapService.Stub.asInterface(bundle?.getBinder("IMAGE_BINDER"))
                getBitmapService.intentBitmap
            }

            bitmap?.apply {
                ivImageShow.setImageBitmap(this)
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
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "多进程大图传递"
        }
    }

}