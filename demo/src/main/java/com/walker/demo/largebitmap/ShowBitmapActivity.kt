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

class ShowBitmapActivity : BaseActivity() {

    private lateinit var ivImageShow: ImageView
    private lateinit var toolbar: Toolbar

    companion object {
        fun start(context: Context, bitmap: Bitmap) {
            Intent(context, ShowBitmapActivity::class.java).also {
                it.putExtras(Bundle().apply {
                    putBinder("IMAGE_BINDER", IntentBinder(bitmap))
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
            title = "同进程大图传递"
        }
    }

    private fun showImageView() {
        val bundle: Bundle? = intent.extras
        val imageBinder: IntentBinder? = bundle?.getBinder("IMAGE_BINDER") as IntentBinder?
        val bitmap = imageBinder?.imageBmp
        bitmap?.apply {
            ivImageShow.setImageBitmap(this)
        }
    }
}