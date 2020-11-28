package com.deadely.itgenglish.di

import android.content.Context
import com.deadely.itgenglish.network.ITGService
import com.deadely.itgenglish.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetModule {
    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(ChuckInterceptor(context))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Singleton
    @Provides
    fun provideRestService(retrofit: Retrofit.Builder): ITGService =
        retrofit.build().create(ITGService::class.java)
}
