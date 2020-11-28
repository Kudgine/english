package com.deadely.itgenglish.network

import com.deadely.itgenglish.model.User
import com.deadely.itgenglish.utils.AUTH
import com.deadely.itgenglish.utils.LOGIN
import com.deadely.itgenglish.utils.SEND_AUDIO
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ITGService {

    @POST(AUTH)
    suspend fun getAuth(@Body user: User): String

    @POST(LOGIN)
    suspend fun login(@Body user: User): String

    @POST(SEND_AUDIO)
    suspend fun sendAudio(
        @Header("Token") token: String,
        @Body body: JsonObject
    ): String
}
