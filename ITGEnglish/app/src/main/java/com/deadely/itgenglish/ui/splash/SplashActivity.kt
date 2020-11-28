package com.deadely.itgenglish.ui.splash

import android.content.Intent
import android.os.Handler
import androidx.activity.viewModels
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseActivity
import com.deadely.itgenglish.ui.login.LoginActivity
import com.deadely.itgenglish.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity(R.layout.activity_splash) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun initObserver() {
        splashViewModel.isLogin.observe(this, {
            if (it) {
                openMainScreen()
            } else {
                openRegisterScreen()
            }
        })
    }

    private fun openRegisterScreen() {
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            },
            2 * 1000.toLong()
        )
    }

    private fun openMainScreen() {
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            },
            2 * 1000.toLong()
        )
    }

    override fun initView() {

    }

    override fun setListeners() {

    }

    override fun getExtras() {

    }
}
