package com.deadely.itgenglish.ui.mapsfragments

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.repository.Repository
import com.deadely.itgenglish.utils.IS_HAS_STAR
import com.deadely.itgenglish.utils.PreferencesManager
import com.deadely.itgenglish.utils.PreferencesManager.get
import dagger.hilt.android.qualifiers.ApplicationContext

class MapCurortViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    BaseViewModel() {

    fun update() {
        mIsHasArtefakt.postValue(preferences.get(IS_HAS_STAR, false))
    }

    private var mIsHasArtefakt = MutableLiveData<Boolean>()
    val isHasArtefakt: LiveData<Boolean> = mIsHasArtefakt

    private val preferences = PreferencesManager.defaultPrefs(context)

    init {
        update()
    }
}
