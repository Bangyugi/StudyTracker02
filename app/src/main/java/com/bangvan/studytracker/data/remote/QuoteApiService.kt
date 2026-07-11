package com.bangvan.studytracker.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface QuoteApiService {

    @GET
    suspend fun getRandomQuote(@Url url: String):   List<QuoteResponse>
}