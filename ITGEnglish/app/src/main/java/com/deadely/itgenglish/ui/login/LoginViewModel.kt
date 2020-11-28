package com.deadely.itgenglish.ui.login

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deadely.itgenglish.R
import com.deadely.itgenglish.base.BaseViewModel
import com.deadely.itgenglish.repository.Repository
import com.deadely.itgenglish.utils.*
import com.deadely.itgenglish.utils.PreferencesManager.get
import com.deadely.itgenglish.utils.PreferencesManager.set
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val preferences = PreferencesManager.defaultPrefs(context)

    private var mValidError = MutableLiveData<String>()
    var validError: LiveData<String> = mValidError

    private var mIsLogin = MutableLiveData<Boolean>()
    var isLogin: LiveData<Boolean> = mIsLogin

    private val mAuthToken = MutableLiveData<DataState<String>>()
    val authToken: LiveData<DataState<String>>
        get() = mAuthToken

    private val mLoginToken = MutableLiveData<DataState<String>>()
    val loginToken: LiveData<DataState<String>>
        get() = mLoginToken

    init {
        mIsLogin.postValue(preferences[IS_LOGIN, false])
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)
                .onEach { dataState -> subscribeData(dataState, GET_LOGIN) }
                .launchIn(viewModelScope)
        }
    }

    fun setMode(isLogin: Boolean) {
        preferences[IS_LOGIN] = isLogin
        mIsLogin.postValue(preferences[IS_LOGIN, false])
    }

    fun getAuthToken(email: String, password: String, years: String, gender: String) {
        viewModelScope.launch {
            repository.getAuth(email, password, years, gender)
                .onEach { dataState -> subscribeData(dataState, GET_AUTH_TOKEN) }
                .launchIn(viewModelScope)
        }
    }

    private fun subscribeData(dataState: DataState<String>, code: String) {
        when (dataState) {
            is DataState.Loading -> {
                when (code) {
                    GET_AUTH_TOKEN -> {
                        mAuthToken.postValue(DataState.Loading)
                    }
                    GET_LOGIN -> {
                        mLoginToken.postValue(DataState.Loading)
                    }
                }
            }
            is DataState.Error -> {
                when (code) {
                    GET_AUTH_TOKEN -> {
                        mAuthToken.postValue(DataState.Error(dataState.exception))
                    }
                    GET_LOGIN -> {
                        mLoginToken.postValue(DataState.Error(dataState.exception))
                    }
                }

            }
            is DataState.Success -> {
                when (code) {
                    GET_AUTH_TOKEN -> {
                        mAuthToken.postValue(DataState.Success(dataState.data))
                    }
                    GET_LOGIN -> {
                        mLoginToken.postValue(DataState.Success(dataState.data))
                    }
                }
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        return when {
            email.trim().isEmpty() -> {
                mValidError.postValue(FieldConverter.getString(R.string.empty_error))
                false
            }
            !email.contains(".") || !email.contains("@") -> {
                mValidError.postValue(FieldConverter.getString(R.string.valid_email_error))
                false
            }
            else -> {
                mValidError.postValue(null)
                true
            }
        }
    }

    fun isFieldValid(years: String): Boolean {
        return when {
            isLogin.value == true -> {
                mValidError.postValue(null)
                true
            }
            years.trim().isEmpty() -> {
                mValidError.postValue(FieldConverter.getString(R.string.empty_error))
                false
            }
            else -> {
                mValidError.postValue(null)
                true
            }
        }
    }

    fun isPasswordValid(password: String): Boolean {
        return when {
            password.trim().isEmpty() -> {
                mValidError.postValue(FieldConverter.getString(R.string.empty_error))
                false
            }
            password.length <= 5 -> {
                mValidError.postValue(FieldConverter.getString(R.string.password_length_email_error))
                false
            }
            else -> {
                mValidError.postValue(null)
                true
            }
        }
    }

    fun setToken(token: String) {
        preferences[TOKEN] = token
    }
}
