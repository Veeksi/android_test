package com.example.testapplication.domain.repository

import androidx.paging.PagingData
import com.example.testapplication.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(): Flow<PagingData<Character>>
}