package com.deadely.itgenglish.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME: String = "crimean_english"
    }

    abstract fun getFavoriteDao(): FavoriteDao
}