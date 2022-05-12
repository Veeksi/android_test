package com.example.testapplication.data.repository

import android.util.Log
import androidx.paging.*
import com.example.testapplication.data.MortyService
import com.example.testapplication.data.data_source.CharactersPagingSource
import com.example.testapplication.data.dto.CharacterDetailDto
import com.example.testapplication.data.util.BaseApiResponse
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val service: MortyService
) : CharacterRepository, BaseApiResponse {
    override fun getCharacters(filter: FilterCharacters): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { CharactersPagingSource(service, filter) }
        ).flow
    }

    override suspend fun getCharacter(id: Int): Flow<Resource<Character>> {
        return flow {
            emit(
                safeApiCall(
                    apiCall = { service.getCharacter(id) },
                    toSomething = CharacterDetailDto::toCharacter
                )
            )
        }.flowOn(Dispatchers.IO)
    }

}