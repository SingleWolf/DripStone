package com.walker.usercenter.login

import android.content.Context
import com.walker.core.base.mvp.PresenterFactory
import com.walker.core.base.mvp.PresenterLoader

object LoginPresenterLoader {
    fun getLoader(context: Context): PresenterLoader<LoginPresenter> {
        return PresenterLoader(context, LoginPresenterFactory());
    }
}

class LoginPresenterFactory : PresenterFactory<LoginPresenter> {
    override fun create(): LoginPresenter {
        return LoginPresenter()
    }

}