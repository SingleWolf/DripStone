package com.walker.dripstone.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walker.core.log.LogHelper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, MainActivity::class.java).let {
            startActivity(it)
        }
        LogHelper.get().d("SplashActivity","onCreate",true)
    }

    override fun onStop() {
        super.onStop()
        LogHelper.get().d("SplashActivity","onStop",true)
        finish()
    }
}