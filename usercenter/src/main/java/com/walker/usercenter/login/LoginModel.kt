package com.walker.usercenter.login

import android.text.TextUtils
import kotlinx.coroutines.delay

class LoginModel {

    companion object {
        const val CODE_SUCCESS = 0;
        const val CODE_INVALID_INFO = 1;
        const val CODE_ERROR = 2;
    }

   suspend fun onLogin(userName: String, password: String): Int {
        delay(5000)
        return if (TextUtils.equals(userName, "walker") && TextUtils.equals(
                password,
                "walker1234"
            )
        ) {
            CODE_SUCCESS
        } else {
            CODE_INVALID_INFO
        }
    }
}