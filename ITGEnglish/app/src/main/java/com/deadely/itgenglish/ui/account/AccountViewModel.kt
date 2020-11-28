package com.deadely.itgenglish.ui.account

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.utils.PreferencesManager
import com.deadely.itgenglish.utils.PreferencesManager.set
import com.deadely.itgenglish.utils.TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext

class AccountViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val preferences = PreferencesManager.defaultPrefs(context)

    fun logout() {
        preferences.set(TOKEN, "")
    }
}
