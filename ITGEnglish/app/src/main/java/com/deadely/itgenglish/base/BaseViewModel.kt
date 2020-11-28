package com.deadely.itgenglish.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    fun addDisposable(disposable: Disposable?) {
        disposable?.let { CompositeDisposable().add(it) }
    }


}