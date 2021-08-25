package com.walker.remoteserver.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.walker.core.log.LogHelper
import com.walker.remoteserver.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvKickOut.setOnClickListener {
            LogHelper.get().i("MainActivity","sendBroadcast")
            val intent = Intent("walker.drip.USER_RECEIVER")
            intent.putExtra("DATA", "远程服务连接成功")
            intent.setPackage("com.walker.dripstone")
            sendBroadcast(intent,"com.walker.drip.permission.ACCESS_REMOTE_SERVER")
        }
    }
}