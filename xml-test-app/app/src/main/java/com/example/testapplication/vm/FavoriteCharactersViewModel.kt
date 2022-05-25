package com.example.testapplication.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCharactersViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _favoriteCharactersFlow = repository.getAllFavoriteCharacters()
    val favoriteCharacters = _favoriteCharactersFlow.asLiveData()

    fun addCharactersToFavorites(characters: List<Character>) {
        viewModelScope.launch {
            repository.addCharactersToFavorite(characters)
        }
    }

    fun deleteCharactersFromFavorites(characters: List<Character>) {
        viewModelScope.launch {
            repository.deleteCharactersFromFavorite(characters)
        }
    }
}