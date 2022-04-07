package com.example.testapplication.data.repository

import android.util.Log
import androidx.paging.*
import com.example.testapplication.data.MortyService
import com.example.testapplication.data.data_source.CharactersPagingSource
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val service: MortyService
) : CharacterRepository {
    override suspend fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { CharactersPagingSource(service) }
        ).flow
    }

    override suspend fun getCharacter(id: Int): Resource<Character> {
        return try {
            val response = service.getCharacter(id)
            val result = response.body()?.toCharacter()

            if (response.isSuccessful && result != null) {
                Resource.Success(data = result)
            } else {
                Resource.Error(errorMessage = response.errorBody().toString())
            }
        } catch (e: HttpException) {
            // Returning HttpException's message
            // wrapped in Resource.Error
            Resource.Error(errorMessage = e.message ?: "Something went wrong")
        } catch (e: IOException) {
            // Returning no internet message
            // wrapped in Resource.Error
            Resource.Error("Please check your network connection")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Something went wrong")
        }
    }
}