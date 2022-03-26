package com.example.testapplication.domain.repository

import com.example.testapplication.api.ApiService

class CharacterRepository(private val apiService: ApiService) {
    suspend fun getCharacters() = apiService.getCharacters()
}