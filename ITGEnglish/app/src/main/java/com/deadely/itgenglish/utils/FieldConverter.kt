package com.deadely.itgenglish.utils

import com.deadely.itgenglish.base.App

object FieldConverter {
    @JvmStatic
    fun getString(id: Int): String {
        return App.instance.resources.getString(id)
    }

    @JvmStatic
    fun getColor(id: Int): Int {
        return App.instance.resources.getColor(id)
    }
}
