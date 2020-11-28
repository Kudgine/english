package com.deadely.itgenglish.ui.favorite

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deadely.itgenglish.model.Word
import com.deadely.itgenglish.repository.Repository
import com.deadely.itgenglish.utils.DataState
import com.deadely.itgenglish.utils.GET_FAVORITE_LIST
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavoriteViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext val context: Context
) :
    ViewModel() {
    private var staticList = listOf<Word>()
    private var mFavoriteList = MutableLiveData<DataState<List<Word>>>()
    var favoriteList: LiveData<DataState<List<Word>>> = mFavoriteList

    private var mFilteredList = MutableLiveData<List<Word>>()
    var filteredList: LiveData<List<Word>> = mFilteredList

    init {
        getFavoritesList()
    }

    private fun getFavoritesList() {
        viewModelScope.launch {
            repository.getFavoritesList()
                .onEach { dataState -> subscribeData(dataState, GET_FAVORITE_LIST) }
                .launchIn(viewModelScope)
        }
    }

    private fun subscribeData(dataState: DataState<List<Word>>, eventCode: String) {
        when (dataState) {
            is DataState.Loading -> {
                when (eventCode) {
                    GET_FAVORITE_LIST -> {
                        mFavoriteList.postValue(DataState.Loading)
                    }
                }
            }
            is DataState.Error -> {
                when (eventCode) {
                    GET_FAVORITE_LIST -> {
                        mFavoriteList.postValue(DataState.Error(dataState.exception))
                    }
                }

            }
            is DataState.Success -> {
                when (eventCode) {
                    GET_FAVORITE_LIST -> {
                        mFavoriteList.postValue(DataState.Success(dataState.data))
                        staticList = dataState.data
                    }
                }
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        if (text.isNotEmpty()) {
            mFilteredList.postValue(staticList.filter {
                it.word.contains(text, ignoreCase = true)
            })
        } else {
            mFilteredList.postValue(staticList)
        }
    }

    fun deleteFavorites(word: Word) {
        repository.deleteFromFavoriteList(word)
        getFavoritesList()
    }
}
