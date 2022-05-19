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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {
    private val modificationEvents = MutableStateFlow<List<PagerEvents>>(emptyList())

    data class EditState(
        val isEditing: Boolean = false,
        val editableCharacters: List<Character> = arrayListOf()
    )

    private val _editState = MutableStateFlow(EditState())
    val editState = _editState.asStateFlow()

    private val _filterCharactersFlow = MutableStateFlow(FilterCharacters())
    val filterCharactersFlow: StateFlow<FilterCharacters>
        get() = _filterCharactersFlow

    private val _favoriteCharactersFlow = MutableStateFlow<List<Character>>(emptyList())
    val favoriteCharactersFlow: StateFlow<List<Character>>
        get() = _favoriteCharactersFlow

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

    fun startEditing() {
        _editState.update { it.copy(isEditing = true) }
    }

    fun stopEditing() {
        _editState.update {
            it.copy(
                isEditing = false,
                editableCharacters = emptyList()
            )
        }
    }

    fun addCharactersToFavorites(characters: List<Character>) {
        viewModelScope.launch {
            /*repository.addFavoriteCharacters(characters)*/
        }
    }

    fun changeSelection(character: Character, selected: Boolean) {
        val oldList = _editState.value.editableCharacters.toList()
        _editState.update {
            it.copy(
                editableCharacters =
                if (selected && !oldList.contains(character)) oldList + character
                else if (!selected && oldList.contains(
                        character
                    )
                ) oldList - character
                else oldList
            )
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
                        if (pagerEvents.character.id == it.id)
                            return@map it.copy(liked = !it.liked)
                        else
                            return@map it
                    }
            }
        }
    }
}