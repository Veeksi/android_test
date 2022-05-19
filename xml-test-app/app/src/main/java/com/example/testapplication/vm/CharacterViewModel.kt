package com.example.testapplication.vm

import androidx.lifecycle.*
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.Episode
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.domain.repository.EpisodeRepository
import com.example.testapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _character = MutableLiveData<Resource<Character>>()
    val character: LiveData<Resource<Character>>
        get() = _character

    private val _episodes = MutableLiveData<ArrayList<Episode>>()
    val episodes: LiveData<ArrayList<Episode>>
        get() = _episodes

    private val _episodeList = arrayListOf<Episode>()
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("id")?.let { id ->
                _isLoading.postValue(true)

                // Start fetching specific character
                characterRepository.getCharacter(id).collect { value ->
                    _character.postValue(value)

                    // Start fetching episodes
                    value.data?.let {
                        it.episodes.map { episode ->
                            val episodeId = parseId(episode)
                            episodeRepository.getEpisode(episodeId).collect { result ->
                                if (result is Resource.Success) {
                                    result.data?.let { it -> _episodeList.add(it) }
                                }
                            }
                        }
                    }
                    _isLoading.postValue(false)
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
