package com.bangvan.studytracker.di

import com.bangvan.studytracker.data.remote.QuoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent ::class)

object NetwordModule {

    private const val BASE_URL = "https://zenquotes.io/api/"

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideQuoteApiService(retrofit: Retrofit) : QuoteApiService{
        return retrofit.create(QuoteApiService::class.java)
    }

}