package com.example.testapplication.vm

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.testapplication.data.dto.CharacterDetailDto
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.Episode
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.domain.repository.EpisodeRepository
import com.example.testapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _character = MutableLiveData<Resource<Character>>()
    val character: LiveData<Resource<Character>> = _character

    private val _episodes = MutableLiveData<ArrayList<Episode>>()
    val episodes: LiveData<ArrayList<Episode>> = _episodes

    private val _episodeList = arrayListOf<Episode>()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("id")?.let { id ->
                isLoading.postValue(true)

                // Start fetching specific character
                characterRepository.getCharacter(id).collect { value ->
                    _character.postValue(value)

                    // Start fetching episodes
                    value.data?.let {
                        it.episodes?.map { episode ->
                            val episodeId = parseId(episode)
                            episodeRepository.getEpisode(episodeId).collect { result ->
                                if (result is Resource.Success) {
                                    result.data?.let { it -> _episodeList.add(it) }
                                }
                            }
                        }
                    }
                    isLoading.postValue(false)
                    _episodes.postValue(_episodeList)
                }
            }
        }
    }

    private fun parseId(url: String): Int {
        val uri = URI(url)
        val segments = uri.path.split("/").toTypedArray()
        val idStr = segments[segments.size - 1]
        return idStr.toInt()
    }
}
