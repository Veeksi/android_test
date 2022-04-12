package com.example.testapplication.domain.repository

import com.example.testapplication.domain.model.Episode
import com.example.testapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    suspend fun getEpisode(id: Int): Flow<Resource<Episode>>
}