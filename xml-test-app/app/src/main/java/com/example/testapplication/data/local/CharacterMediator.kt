package com.example.testapplication.data.local

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.testapplication.data.MortyService
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.domain.model.RemoteKeys
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class CharacterMediator(
    private val mortyService: MortyService,
    private val database: CharacterDatabase,
    private val filter: FilterCharacters
) : RemoteMediator<Int, Character>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = mortyService.getCharacters(
                page = page,
                status = filter.status.value,
                name = filter.name,
                gender = filter.gender.value,
            ).toPagedResponseCharacter()
            val isEndOfList = response.pageInfo.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    with(database) {
                        charactersRemoteKeyDao().clearRemoteKeys()
                        charactersDao().clearAllCharacters()
                    }
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    RemoteKeys(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.charactersRemoteKeyDao().insertAll(keys)
                database.charactersDao().insertAll(response.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
        }
    }

    // Gets first remote key which contains data
    // Used for LoadType.PREPEND
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Character>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { character ->
                database.charactersRemoteKeyDao().remoteKeysCharacterId(character.id)
            }
    }

    // Gets last remote key which contains data
    // Used for LoadType.APPEND
    private suspend fun getLastRemoteKey(state: PagingState<Int, Character>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { character ->
                database.charactersRemoteKeyDao().remoteKeysCharacterId(character.id)
            }
    }

    // Gets closest remote key which contains data
    // Used for LoadType.REFRESH
    private suspend fun getClosestRemoteKey(state: PagingState<Int, Character>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterId ->
                database.charactersRemoteKeyDao().remoteKeysCharacterId(characterId)
            }
        }
    }

}