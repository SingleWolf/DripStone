package com.walker.common.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import com.walker.common.R
import com.walker.common.databinding.ActivityCommonShowBinding
import com.walker.common.fragment.EmptyFragment

class ShowActivity : AppCompatActivity() {

    private lateinit var viewDataBinding: ActivityCommonShowBinding

    private lateinit var channelId: String
    private lateinit var channelName: String

    companion object {
        const val KEY_PARAM_CHANNEL_ID = "key_param_channel_id"

        const val KEY_PARAM_CHANNEL_NAME = "key_param_channel_name"

        fun start(context: Context, channelId: String, channelName: String) {
            if (TextUtils.isEmpty(channelId)) {
                return
            }
            Intent(context, ShowActivity::class.java).let {
                it.putExtra(KEY_PARAM_CHANNEL_ID, channelId)
                it.putExtra(KEY_PARAM_CHANNEL_NAME, channelName)
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
        viewDataBinding = setContentView(this, R.layout.activity_common_show)
        channelId = intent.getStringExtra(KEY_PARAM_CHANNEL_ID)
        channelName = intent.getStringExtra(KEY_PARAM_CHANNEL_NAME)
        initToolbar()
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
        setSupportActionBar(viewDataBinding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = channelName
        }
    }

    private fun setupFragment(fragment: Fragment) {
        fragment?.let {
            val manger = supportFragmentManager
            val transaction = manger.beginTransaction()
            transaction.add(R.id.container, fragment, fragment.javaClass.name).commit()
        }
    }
}