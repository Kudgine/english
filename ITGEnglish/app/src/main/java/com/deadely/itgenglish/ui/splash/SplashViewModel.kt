package com.deadely.itgenglish.ui.splash

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.utils.IS_LOGIN
import com.deadely.itgenglish.utils.PreferencesManager
import com.deadely.itgenglish.utils.PreferencesManager.get
import com.deadely.itgenglish.utils.PreferencesManager.set
import com.deadely.itgenglish.utils.TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext


class SplashViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val preferences = PreferencesManager.defaultPrefs(context)

    private var mIsLogin = MutableLiveData<Boolean>()
    var isLogin: LiveData<Boolean> = mIsLogin

    init {
        preferences.set(IS_LOGIN, isToken())
        mIsLogin.postValue(preferences[IS_LOGIN, false])
    }

    private fun isToken(): Boolean = preferences[TOKEN, ""] != ""
}
