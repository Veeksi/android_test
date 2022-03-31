package com.example.testapplication.data

import com.example.testapplication.data.dto.CharacterDto
import com.example.testapplication.data.dto.PagedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MortyService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): PagedResponseDto<CharacterDto>
}