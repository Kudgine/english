package com.deadely.itgenglish.model

import com.google.gson.annotations.SerializedName


data class Sound(
    @SerializedName("content")
    var content: String
)
