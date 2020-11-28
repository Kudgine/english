package com.deadely.itgenglish.database

import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites_table")
    suspend fun getAllFavos(): List<FavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavos(word: FavoriteEntity)

    @Delete
    fun deleteFavos(word: FavoriteEntity)
}