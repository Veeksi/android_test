package com.example.testapplication.data.repository

import com.example.testapplication.data.MortyService
import com.example.testapplication.data.dto.EpisodeDto
import com.example.testapplication.data.util.BaseApiResponse
import com.example.testapplication.domain.model.Episode
import com.example.testapplication.domain.repository.EpisodeRepository
import com.example.testapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val service: MortyService
) : EpisodeRepository, BaseApiResponse {
    override suspend fun getEpisode(id: Int): Flow<Resource<Episode>> {
        return flow {
            emit(
                safeApiCall(
                    apiCall = { service.getEpisode(id) },
                    toSomething = EpisodeDto::toEpisode
                )
            )
        }.flowOn(Dispatchers.IO)
    }

}