package com.example.testapplication.vm

import android.util.Log
import androidx.lifecycle.*

import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _character = MutableLiveData<Resource<Character>>()
    val character: LiveData<Resource<Character>> = _character

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch() {
            savedStateHandle.get<Int>("id")?.let {
                _character.postValue(Resource.Loading())
                _character.postValue(repository.getCharacter(it))
            }
        }
    }
}
