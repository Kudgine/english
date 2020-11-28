package com.deadely.itgenglish.ui.grammar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.deadely.itgenglish.R
import com.deadely.itgenglish.extensions.setActivityTitle

class GrammarFragment : Fragment(R.layout.fragment_grammar) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setActivityTitle(R.string.grammar)
    }
}
