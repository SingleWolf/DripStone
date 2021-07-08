package com.walker.usercenter.login

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
    }

    suspend fun onLogin(userName: String, password: String) {
        loginView.showProgress()
        withContext(Dispatchers.Default) {}
        var result = loginModel.onLogin(userName, password)
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