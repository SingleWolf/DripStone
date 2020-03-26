package com.walker.dripstone.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walker.core.log.LogUtils

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, MainActivity::class.java).let {
            startActivity(it)
        }
        LogUtils.d("SplashActivity","onCreate",true)
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d("SplashActivity","onStop",true)
        finish()
    }
}