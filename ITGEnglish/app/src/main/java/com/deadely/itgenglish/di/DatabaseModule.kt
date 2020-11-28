package com.deadely.itgenglish.di

import android.content.Context
import androidx.room.Room
import com.deadely.itgenglish.database.AppDatabase
import com.deadely.itgenglish.database.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideFavoriteDao(appDataBase: AppDatabase): FavoriteDao {
        return appDataBase.getFavoriteDao()
    }
}
