package com.deadely.itgenglish.ui.account

import android.view.MenuItem
import androidx.activity.viewModels
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseActivity
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.utils.FieldConverter
import com.deadely.itgenglish.utils.LOGOUT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_account.*

@AndroidEntryPoint
class AccountActivity : BaseActivity(R.layout.activity_account) {

    private val viewModel: AccountViewModel by viewModels()

    override fun initView() {
        title = FieldConverter.getString(R.string.account_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setListeners() {
        rlPassword.setOnClickListener { snack(rlExit, "Сейчас функция смены пароля недоступна") }
        rlEmail.setOnClickListener { snack(rlExit, "Сейчас функция смены email недоступна") }
        rlGender.setOnClickListener { snack(rlExit, "Сейчас функция смены гендера недоступна") }
        rlCategory.setOnClickListener { snack(rlExit, "Сейчас функция смены категории недоступна") }
        rlExit.setOnClickListener {
            viewModel.logout()
            setResultLogout()
        }
    }

    private fun setResultLogout() {
        setResult(LOGOUT)
        finish()
    }

    override fun initObserver() {

    }

    override fun getExtras() {

    }
}