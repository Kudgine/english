package com.deadely.itgenglish.ui.login

import android.content.Intent
import android.graphics.Paint
import androidx.activity.viewModels
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseActivity
import com.deadely.itgenglish.extensions.makeGone
import com.deadely.itgenglish.extensions.makeVisible
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.ui.main.MainActivity
import com.deadely.itgenglish.utils.DataState
import com.deadely.itgenglish.utils.EMAIL_IS_USED
import com.deadely.itgenglish.utils.FieldConverter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*


@AndroidEntryPoint
class LoginActivity : BaseActivity(R.layout.activity_login) {

    private val loginViewModel: LoginViewModel by viewModels()

    private var isLogin = false

    override fun initView() {
        supportActionBar?.hide()
        if (isLogin) {
            showLoginMode()
        } else {
            showRegMode()
        }
        tvUno.paintFlags = tvUno.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    override fun setListeners() {
        tvUno.setOnClickListener {
            loginViewModel.setMode(!isLogin)
        }
        btnLogin.setOnClickListener {
            if (loginViewModel.isEmailValid(etEmail.text.toString()) && loginViewModel.isPasswordValid(
                    etPassword.text.toString()
                ) &&
                loginViewModel.isFieldValid(etYear.text.toString()) && loginViewModel.isFieldValid(
                    etGender.text.toString()
                )
            ) {
                if (isLogin) {
                    loginViewModel.login(etEmail.text.toString(), etPassword.text.toString())
                } else {
                    loginViewModel.getAuthToken(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etYear.text.toString(),
                        etGender.text.toString()
                    )
                }
            }
        }
    }

    override fun initObserver() {
        loginViewModel.validError.observe(this, {
            it?.let {
                snack(btnLogin, it)
            }
        })
        loginViewModel.isLogin.observe(this, {
            isLogin = it
            if (isLogin) {
                showLoginMode()
            } else {
                showRegMode()
            }
        })
        loginViewModel.authToken.observe(this, {
            when (it) {
                is DataState.Loading -> {
                    hideContent()
                }
                is DataState.Error -> {
                    it.exception.printStackTrace()
                    showContent()
                    snack(btnLogin, FieldConverter.getString(R.string.default_error))
                }
                is DataState.Success -> {
                    showContent()
                    if (it.data == EMAIL_IS_USED) {
                        snack(btnLogin, R.string.email_is_already_used)
                    } else {
                        loginViewModel.setToken(it.data)
                        openMainScreen()
                    }
                }
            }
        })
        loginViewModel.loginToken.observe(this, {
            when (it) {
                is DataState.Loading -> {
                    hideContent()
                }
                is DataState.Error -> {
                    it.exception.printStackTrace()
                    hideContent()
                    snack(btnLogin, FieldConverter.getString(R.string.default_error))
                }
                is DataState.Success -> {
                    showContent()
                    if (it.data == EMAIL_IS_USED) {
                        snack(btnLogin, R.string.error_on_auth)

                    } else {
                        loginViewModel.setToken(it.data)
                        openMainScreen()
                    }
                }
            }
        }
        )
    }

    private fun hideContent() {
        pvLoad.makeVisible()
        btnLogin.makeGone()
        tvUno.makeGone()
        rlFields.makeGone()
    }

    private fun showContent() {
        pvLoad.makeGone()
        btnLogin.makeVisible()
        tvUno.makeVisible()
        rlFields.makeVisible()
    }

    private fun showLoginMode() {
        btnLogin.text = getString(R.string.login)
        tvUno.text = getString(R.string.account_no_exist)
        rlYear.makeGone()
        etYear.makeGone()
        rlGender.makeGone()
        etGender.makeGone()
    }

    private fun showRegMode() {
        btnLogin.text = getString(R.string.register)
        tvUno.text = getString(R.string.is_account_exist)
        rlYear.makeVisible()
        etYear.makeVisible()
        rlGender.makeVisible()
        etGender.makeVisible()
    }

    private fun openMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getExtras() {}
}
