package com.deadely.itgenglish.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: String?,
    val lessons: List<Lessons>?
) : Parcelable

@Parcelize
data class Lessons(
    val id: String?,
    val title: String?,
    val asks: List<Ask>?
) : Parcelable

@Parcelize
data class Ask(
    val id: String?,
    val text: String?
) : Parcelable

