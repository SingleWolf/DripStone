package com.walker.usercenter.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walker.usercenter.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, LoginFragment(), "LoginFragment").commit()
    }
}