package com.deadely.itgenglish.ui.firstscrene

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.repository.Repository
import com.deadely.itgenglish.utils.IS_HAS_PEANUT_BUTTER
import com.deadely.itgenglish.utils.PreferencesManager
import com.deadely.itgenglish.utils.PreferencesManager.set
import dagger.hilt.android.qualifiers.ApplicationContext

class ConditionViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    BaseViewModel() {

    private val preferences = PreferencesManager.defaultPrefs(context)

    fun setArtefakt() {
        preferences.set(IS_HAS_PEANUT_BUTTER, true)
    }
}
