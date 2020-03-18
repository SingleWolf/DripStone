package com.walker.dripstone.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, MainActivity::class.java).let {
            startActivity(it)
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}