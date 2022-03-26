package com.example.testapplication.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: CharacterRepository) : ViewModel() {
    private val characterLiveData = MutableLiveData<List<Character>>()

    fun getCharacter() = characterLiveData

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("MainViewModel", "Exception: $exception")
        }

        viewModelScope.launch(handler) {
            Log.d("MainViewModel", "Getting characters")
            val characters = repository.getCharacters()
            Log.d("MainViewModel", "Characters")

            when (characters.isSuccessful) {
                true -> {
                    Log.d("MainViewModel", characters.body().toString())
                    with(characters.body()?.results.orEmpty()) {
                        var characterList = listOf<Character>()

                        forEach { character ->
                            characterList = characterList + Character(character.name, character.image, character.gender)
                        }

                        characterLiveData.postValue(characterList)
                    }
                }
                else -> {
                    Log.d("MainViewModel", "Error")
                }
            }
        }
    }
}