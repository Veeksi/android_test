package com.example.testapplication.data.repository

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.testapplication.data.MortyService
import com.example.testapplication.data.remote.CharactersPagingSource
import com.example.testapplication.data.dto.CharacterDetailDto
import com.example.testapplication.data.local.CharacterDatabase
import com.example.testapplication.data.local.CharacterMediator
import com.example.testapplication.data.util.BaseApiResponse
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val service: MortyService,
    private val characterDatabase: CharacterDatabase
) : CharacterRepository, BaseApiResponse {
    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(filter: FilterCharacters): Flow<PagingData<Character>> {
        val pagingSourceFactory = {
            characterDatabase.charactersDao()
                .getAllCharacters(filter.name, filter.gender.value, filter.status.value)
        }
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = pagingSourceFactory,
            /*
            Old PagingSource when only online support
            pagingSourceFactory = { CharactersPagingSource(service, filter) },
            */
            remoteMediator = CharacterMediator(service, characterDatabase, filter)
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