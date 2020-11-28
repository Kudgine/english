package com.deadely.itgenglish.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment(layout: Int) : Fragment(layout) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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