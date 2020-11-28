package com.deadely.itgenglish.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(layout: Int) : AppCompatActivity(layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtras()
        initObserver()
        initView()
        setListeners()
    }

    abstract fun initView()
    abstract fun setListeners()
    abstract fun initObserver()
    abstract fun getExtras()
}