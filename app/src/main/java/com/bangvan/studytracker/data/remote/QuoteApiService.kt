package com.bangvan.studytracker.data.remote

import com.bangvan.studytracker.di.UrlConfig
import retrofit2.http.GET
import retrofit2.http.Url

interface QuoteApiService {

    @GET(UrlConfig.QUOTE_PATH)
    suspend fun getRandomQuote():   List<QuoteResponse>
}