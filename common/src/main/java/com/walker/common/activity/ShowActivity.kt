package com.walker.common.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.walker.common.R
import com.walker.common.fragment.EmptyFragment
import com.walker.core.log.LogHelper
import kotlinx.android.synthetic.main.activity_common_show.*

class ShowActivity : AppCompatActivity() {

    private lateinit var channelId: String
    private lateinit var channelName: String

    companion object {
        const val TAG = "ShowActivity"
        const val KEY_PARAM_CHANNEL_ID = "key_param_channel_id"

        const val KEY_PARAM_CHANNEL_NAME = "key_param_channel_name"

        var taskFunction: ((channelId: String) -> String?)? = null

        fun start(
            context: Context,
            channelId: String,
            channelName: String,
            task: ((channelId: String) -> String?)?
        ) {
            if (TextUtils.isEmpty(channelId)) {
                return
            }

            Intent(context, ShowActivity::class.java).let {
                it.putExtra(KEY_PARAM_CHANNEL_ID, channelId)
                it.putExtra(KEY_PARAM_CHANNEL_NAME, channelName)
                taskFunction = task
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
        setContentView(R.layout.activity_common_show)
        channelId = intent.getStringExtra(KEY_PARAM_CHANNEL_ID)!!
        channelName = intent.getStringExtra(KEY_PARAM_CHANNEL_NAME)!!
        initToolbar()
        initFragment()
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
            title = channelName
        }
    }

    private fun initFragment() {
        var fragmentClazz: String? = taskFunction?.invoke(channelId)
        var fragment = findFragmentByClazz(fragmentClazz)
        fragment ?: let { fragment = EmptyFragment.instance(channelName) }
        val manger = supportFragmentManager
        val transaction = manger.beginTransaction()
        transaction.add(R.id.container, fragment!!, fragment!!.javaClass.name).commit()
    }

    private fun findFragmentByClazz(fragmentClazz: String?): Fragment? {
        var fragment: Fragment? = null
        fragmentClazz?.apply {
            try {
                Class.forName(this)?.newInstance()?.also {
                    if (it is Fragment) {
                        fragment = it
                    }
                }
            } catch (e: java.lang.Exception) {
                LogHelper.get().e(TAG, "findFragmentByClazz error : $e")
            }
        }
        return fragment
    }
}