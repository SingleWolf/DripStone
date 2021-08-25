package com.walker.usercenter.login

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import com.walker.core.base.mvp.BaseMvpFragment
import com.walker.core.base.mvp.PresenterLoader
import com.walker.core.util.ToastUtils
import com.walker.usercenter.R
import kotlinx.coroutines.launch

class LoginFragment : BaseMvpFragment<LoginPresenter>(), LoginView {

    private lateinit var edtUserName: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar

    override fun setAttachView() = this

    override fun setLayoutResId() = R.layout.fragment_user_login

    override fun setPresenterLoader(): PresenterLoader<LoginPresenter> {
        return LoginPresenterLoader.getLoader(mActivity)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissProgress() {
        progressBar.visibility = View.GONE
    }

    override fun loginSuccess() {
        ToastUtils.showCenter("登录成功")
        mActivity?.finish()
    }

    override fun loginError(errCode: Int) {
        when (errCode) {
            LoginModel.CODE_INVALID_INFO -> {
                ToastUtils.showCenter("用户信息有误")
            }
            LoginModel.CODE_CONN_ERROR -> {
                ToastUtils.showCenter("服务连接失败")
            }
            else -> {
                ToastUtils.showCenter("登录异常")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtUserName = view.findViewById(R.id.edtUserName)
        edtPassword = view.findViewById(R.id.edtPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        progressBar = view.findViewById(R.id.loginProgress)
        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        lifecycleScope.launch {
            presenter?.onLogin(
                requireActivity(),
                edtUserName.text.toString(),
                edtPassword.text.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.execRelease()
    }
}