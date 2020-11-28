package com.deadely.itgenglish.ui.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseActivity
import com.deadely.itgenglish.extensions.snack
import com.deadely.itgenglish.ui.account.AccountActivity
import com.deadely.itgenglish.ui.login.LoginActivity
import com.deadely.itgenglish.utils.LOGOUT
import com.deadely.itgenglish.utils.OPEN_ACCOUNT_SCREEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun initView() {
        setSupportActionBar(toolbar)
    }

    override fun setListeners() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navController = findNavController(R.id.navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_lessons, R.id.nav_dictionary, R.id.nav_grammar, R.id.nav_favorite
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_personal_account -> {
                startActivityForResult(
                    Intent(this, AccountActivity::class.java),
                    OPEN_ACCOUNT_SCREEN
                )
                return true
            }
            R.id.action_support -> {
                snack(navView, R.string.support_doesnt_work)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navController)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun initObserver() {}

    override fun getExtras() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            OPEN_ACCOUNT_SCREEN -> {
                when (resultCode) {
                    LOGOUT -> {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
