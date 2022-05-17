package com.example.testapplication.vm

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.PagerEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {
    private val modificationEvents = MutableStateFlow<List<PagerEvents>>(emptyList())

    data class ViewState(
        val isEditing: Boolean = false,
        val editableCharacters: List<Character> = arrayListOf()
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val _filterCharactersFlow = MutableStateFlow(FilterCharacters())
    val filterCharactersFlow: StateFlow<FilterCharacters>
        get() = _filterCharactersFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _charactersFlow: Flow<PagingData<Character>> =
        _filterCharactersFlow.flatMapLatest { filter ->
            repository.getCharacters(filter).cachedIn(viewModelScope)
                .combine(modificationEvents) { pagingData, modifications ->
                    modifications.fold(pagingData) { acc, event ->
                        applyEvents(acc, event)
                    }
                }
        }.cachedIn(viewModelScope)

    val charactersFlow: Flow<PagingData<Character>>
        get() = _charactersFlow

    fun onFiltersChange(filter: FilterCharacters) {
        _filterCharactersFlow.value = filter
    }

    fun startEditing(character: Character) {
        _viewState.update {
            it.copy(isEditing = true)
        }
        addOrRemoveEditableCharacter(character)
    }

    fun stopEditing() {
        _viewState.update {
            it.copy(isEditing = false)
        }
        clearEditableCharacters()
    }

    fun addOrRemoveEditableCharacter(character: Character) {
        if (_viewState.value.editableCharacters.contains(character)) {
            _viewState.update {
                val oldList = it.editableCharacters.toList()
                val newList = oldList.filter { filteredCharacter ->
                    filteredCharacter.id != character.id
                }
                it.copy(editableCharacters = newList)
            }
            if (_viewState.value.editableCharacters.isEmpty()) {
                _viewState.update { it.copy(isEditing = false) }
            }
        } else {
            _viewState.update {
                val newList = it.editableCharacters.toList()
                it.copy(editableCharacters = newList + arrayListOf(character))
            }
        }
    }

    private fun clearEditableCharacters() {
        _viewState.update {
            it.copy(editableCharacters = arrayListOf())
        }
    }

    fun onViewEvent(pagerEvents: PagerEvents) {
        modificationEvents.value += pagerEvents
    }

    private fun applyEvents(
        paging: PagingData<Character>,
        pagerEvents: PagerEvents
    ): PagingData<Character> {
        return when (pagerEvents) {
            is PagerEvents.Remove -> {
                paging
                    .filter {
                        pagerEvents.character.id != it.id
                    }
            }
            is PagerEvents.Like -> {
                paging
                    .map {
                        if (pagerEvents.character.id == it.id) return@map it.copy(liked = !it.liked)
                        else return@map it
                    }
            }
        }
    }
}