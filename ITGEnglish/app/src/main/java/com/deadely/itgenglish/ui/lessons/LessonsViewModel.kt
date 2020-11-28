package com.deadely.itgenglish.ui.lessons

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext

class LessonsViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) :
    BaseViewModel()