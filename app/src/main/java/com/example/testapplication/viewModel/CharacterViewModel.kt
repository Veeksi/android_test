package com.example.testapplication.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.testapplication.data.repository.CharacterRepositoryImpl
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepository) : ViewModel() {
    private lateinit var _charactersFlow: Flow<PagingData<Character>>
    val charactersFlow: Flow<PagingData<Character>>
        get() = _charactersFlow

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("CharacterViewModel", "Exception: $exception")
        }

        viewModelScope.launch(handler) {
            _charactersFlow = repository.getCharacters().cachedIn(viewModelScope)
        }
    }
}