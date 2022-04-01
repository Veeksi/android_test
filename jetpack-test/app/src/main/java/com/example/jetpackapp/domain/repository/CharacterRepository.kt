package com.example.jetpackapp.domain.repository

import androidx.paging.PagingData
import com.example.jetpackapp.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(): Flow<PagingData<Character>>
}