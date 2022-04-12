package com.example.testapplication.data

import com.example.testapplication.data.dto.CharacterDetailDto
import com.example.testapplication.data.dto.CharacterDto
import com.example.testapplication.data.dto.EpisodeDto
import com.example.testapplication.data.dto.PagedResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MortyService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): PagedResponseDto<CharacterDto>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<CharacterDetailDto>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Response<EpisodeDto>
}