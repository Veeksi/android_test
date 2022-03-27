package com.example.testapplication.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testapplication.data.MortyService
import com.example.testapplication.data.data_source.CharactersPagingSource
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val service: MortyService
) : CharacterRepository {
    override suspend fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { CharactersPagingSource(service)}
        ).flow
    }
}