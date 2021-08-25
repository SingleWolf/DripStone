package com.walker.usercenter.login

import android.app.Activity
import android.content.Context
import com.walker.core.base.mvp.IBasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginPresenter : IBasePresenter<LoginView> {

    private lateinit var loginView: LoginView
    private lateinit var loginModel: LoginModel

    override fun attachView(view: LoginView) {
        loginView = view
        loginModel = LoginModel()
    }

    override fun getAttachView(): LoginView {
        return loginView
    }

    override fun detachView() {
    }

    override fun execRelease() {
        loginModel.execRelease()
    }

    suspend fun onLogin(context:Activity,userName: String, password: String) {
        loginView.showProgress()
        var result = loginModel.onLogin(context,userName, password)
        withContext(Dispatchers.Main) {
            if (result == LoginModel.CODE_SUCCESS) {
                loginView.loginSuccess()
            } else {
                loginView.loginError(result)
            }
        }
        loginView.dismissProgress()
    }

}