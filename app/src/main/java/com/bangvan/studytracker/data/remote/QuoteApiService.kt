package com.bangvan.studytracker.data.remote

import retrofit2.http.GET

interface QuoteApiService {
    @GET("random")
    suspend fun getRandomQuote():   List<QuoteResponse>
}