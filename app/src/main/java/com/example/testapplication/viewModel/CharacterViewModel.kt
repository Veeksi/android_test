package com.example.testapplication.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.testapplication.data.repository.CharacterRepositoryImpl
import com.example.testapplication.domain.model.Character
import com.example.testapplication.domain.repository.CharacterRepository
import com.example.testapplication.util.PagerEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(private val repository: CharacterRepository) : ViewModel() {
    private val modificationEvents = MutableStateFlow<List<PagerEvents>>(emptyList())

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
            _charactersFlow = repository.getCharacters().cachedIn(viewModelScope).combine(modificationEvents) { pagingData, modifications ->
                modifications.fold(pagingData) {acc, event ->
                    applyEvents(acc, event)
                }
            }
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
                    .filter { pagerEvents.character.id != it.id }
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