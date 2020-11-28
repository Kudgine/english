package com.deadely.itgenglish.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("Email") val Email: String?,
    @SerializedName("Password") val Password: String?,
    @SerializedName("Years") val Years: String?,
    @SerializedName("Gender") val Gender: String?,
) : Parcelable {
    constructor(email: String?, gender: String?) : this(email, gender, null, null)
}
