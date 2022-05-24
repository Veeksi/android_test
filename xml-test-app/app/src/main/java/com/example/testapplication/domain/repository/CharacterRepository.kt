package com.example.testapplication.domain.repository

import androidx.paging.PagingData
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(filter: FilterCharacters): Flow<PagingData<Character>>
    suspend fun getCharacter(id: Int): Flow<Resource<Character>>
    suspend fun addCharactersToFavorite(characters: List<Character>)
    suspend fun getAllFavoriteCharacters(): List<Character>
    suspend fun deleteCharactersFromFavorite(characters: List<Character>)
}