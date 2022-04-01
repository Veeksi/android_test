package com.example.jetpackapp.domain.data

import com.example.jetpackapp.domain.data.dto.CharacterDto
import com.example.jetpackapp.domain.data.dto.PagedResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MortyService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): PagedResponseDto<CharacterDto>
}