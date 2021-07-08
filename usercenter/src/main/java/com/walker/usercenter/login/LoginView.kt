package com.walker.usercenter.login

import com.walker.core.base.mvp.IBaseView

interface LoginView : IBaseView {
    fun showProgress()
    fun dismissProgress()
    fun loginSuccess()
    fun loginError(errCode: Int)
}