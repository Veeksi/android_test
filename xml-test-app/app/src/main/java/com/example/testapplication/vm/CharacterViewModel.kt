package com.example.testapplication.vm

import android.util.Log
import androidx.lifecycle.*
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.Episode
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.domain.repository.EpisodeRepository
import com.example.testapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    data class UiState(
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val errorMessage: String? = null,
        val episodes: List<Episode> = emptyList(),
        val character: Resource<Character>? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _tempEpisodeList = arrayListOf<Episode>()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            savedStateHandle.get<Int>("id")?.let { id ->
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        hasError = false,
                        errorMessage = null
                    )
                }

                // Fetch specific character
                characterRepository.getCharacter(id).collect { character ->
                    if (character is Resource.Error) {
                        _uiState.update {
                            it.copy(hasError = true, errorMessage = character.errorMessage)
                        }
                    }

                    _uiState.update {
                        it.copy(character = character)
                    }

                    // Fetch character episodes
                    character.data?.let {
                        it.episodes.map { episode ->
                            val episodeId = parseId(episode)
                            episodeRepository.getEpisode(episodeId).collect { result ->
                                if (result is Resource.Success) {
                                    result.data?.let { it -> _tempEpisodeList.add(it) }
                                } else if (result is Resource.Error) {
                                    _uiState.update { state ->
                                        state.copy(
                                            isLoading = false,
                                            hasError = true,
                                            episodes = _tempEpisodeList,
                                            errorMessage = "Not all data is up-to-date, please consider refreshing!"
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Emit episodes after all of them are loaded
                    _uiState.update {
                        it.copy(isLoading = false, episodes = _tempEpisodeList)
                    }
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
