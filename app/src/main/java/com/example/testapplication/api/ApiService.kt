package com.example.testapplication.api

import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.PagedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): Response<PagedResponse<Character>>
}