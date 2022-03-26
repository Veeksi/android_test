package com.example.testapplication.api

import com.example.testapplication.domain.model.Characters
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("character/?page=1")
    suspend fun getCharacters(): Response<Characters>
}