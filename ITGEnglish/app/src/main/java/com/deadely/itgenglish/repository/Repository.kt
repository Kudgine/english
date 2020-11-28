package com.deadely.itgenglish.repository

import android.os.Build
import com.deadely.itgenglish.database.AppDatabase
import com.deadely.itgenglish.database.mapToDBEntity
import com.deadely.itgenglish.database.mapToModelList
import com.deadely.itgenglish.model.User
import com.deadely.itgenglish.model.Word
import com.deadely.itgenglish.network.ITGService
import com.deadely.itgenglish.utils.DataState
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.util.Base64.getEncoder
import javax.inject.Inject


class Repository @Inject constructor(private val api: ITGService, private val db: AppDatabase) {

    suspend fun getAuth(
        email: String,
        password: String,
        year: String,
        gender: String
    ): Flow<DataState<String>> = flow {
        try {
            emit(DataState.Loading)
            val token = api.getAuth(User(email, password, year, gender))
            emit(DataState.Success(token))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun login(email: String, password: String): Flow<DataState<String>> = flow {
        try {
            emit(DataState.Loading)
            val token = api.login(User(email, password))
            emit(DataState.Success(token))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getFavoritesList(): Flow<DataState<List<Word>>> = flow {
        try {
            emit(DataState.Loading)
            val favoritesList = db.getFavoriteDao().getAllFavos().mapToModelList()
            emit(DataState.Success(favoritesList))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    fun addToFavoriteList(word: Word) {
        db.getFavoriteDao().addFavos(word.mapToDBEntity())
    }

    fun deleteFromFavoriteList(word: Word) {
        db.getFavoriteDao().deleteFavos(word.mapToDBEntity())
    }

    suspend fun sendAudio(token: String, audioFile: File): Flow<DataState<String>> = flow {
        try {
            emit(DataState.Loading)
            val bytes = audioFile.readBytes()
            var base64 = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                base64 = getEncoder().encodeToString(bytes)
            }

            val value = JsonObject()
            value.addProperty("content", base64)

            val data: String = api.sendAudio(
                token,
                value
            )
            if (data is String) {
                emit(DataState.Success(data))
            } else {
                emit(DataState.Error(java.lang.Exception("Не получилось распознать аудиосообщение. Попробуйте еще раз")))
            }
        } catch (e: Exception) {
            if (e is NullPointerException) {
                emit(DataState.Success(""))
            } else {
                emit(DataState.Error(e))
            }
        }
    }
}