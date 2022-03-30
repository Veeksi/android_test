package com.example.testapplication.data.repository

import androidx.paging.*
import com.example.testapplication.data.MortyService
import com.example.testapplication.data.data_source.CharactersPagingSource
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.PagerEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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