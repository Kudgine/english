package com.deadely.itgenglish.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Word(
    @SerializedName("id") val id: String,
    @SerializedName("word") val word: String,
    @SerializedName("tr") val tr: String,
    @SerializedName("translate") val translate: String,
    @Transient var favorite: Boolean
) : Parcelable
