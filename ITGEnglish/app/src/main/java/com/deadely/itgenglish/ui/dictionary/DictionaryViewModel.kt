package com.deadely.itgenglish.ui.dictionary

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deadely.itgenglish.model.Word
import com.deadely.itgenglish.repository.Repository
import com.deadely.itgenglish.utils.DataState
import com.deadely.itgenglish.utils.GET_FAVORITE_LIST
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DictionaryViewModel @ViewModelInject constructor(private val repository: Repository) :
    ViewModel() {

    private var mFavoriteList = MutableLiveData<DataState<List<Word>>>()
    var favoriteList: LiveData<DataState<List<Word>>> = mFavoriteList

    private var mListWords = MutableLiveData<List<Word>>()
    var listWords: LiveData<List<Word>> = mListWords

    private var staticList = mutableListOf(
        Word("0", "apple", "[æpl]", "яблоко", false),
        Word("1", "rain", "[reɪn]", "дождь", false),
        Word("2", "human", "[ˈhjuːmən]", "человек", false),
        Word("3", "you", "[juː]", "ты", false),
        Word("4", "light", "[laɪt]", "свет", false),
        Word("5", "word", "[wɜːd]", "слово", false),
        Word("6", "block", "[blɒk]", "блок", false),
        Word("7", "dialogue", "[ˈdaɪəlɒg]", "диалог", false),
        Word("8", "color", "[ˈkʌlə]", "цвет", false)
    )

    init {
        viewModelScope.launch {
            repository.getFavoritesList()
                .onEach { dataState -> subscribeData(dataState, GET_FAVORITE_LIST) }
                .launchIn(viewModelScope)
        }
    }

    private fun getListWord() {
        mListWords.postValue(staticList)
    }

    fun onSearchTextChanged(text: String) {
        var filteredList = listOf<Word>()
        if (text.isNotEmpty()) {
            filteredList = staticList.filter {
                it.word.contains(text, ignoreCase = true)
            }
            mListWords.postValue(filteredList)
        } else {
            mListWords.postValue(staticList)
        }
    }

    fun deleteFavorites(word: Word) {
        repository.deleteFromFavoriteList(word)
    }

    fun addFavorite(word: Word) {
        repository.addToFavoriteList(word)
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
                        val mergedList = staticList
                        dataState.data.forEach {
                            it.favorite = !it.favorite
                            if (staticList.contains(it)) {
                                staticList.remove(it)
                                it.favorite = !it.favorite
                                staticList.add(it)
                            }
                        }
                        mFavoriteList.postValue(DataState.Success(mergedList))
                    }
                }
            }
        }
    }
}
