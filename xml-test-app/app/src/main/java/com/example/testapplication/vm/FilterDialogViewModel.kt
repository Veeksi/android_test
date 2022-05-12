package com.example.testapplication.vm

import androidx.lifecycle.ViewModel
import com.example.testapplication.domain.model.CharacterStatus
import com.example.testapplication.domain.model.FilterCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FilterDialogViewModel @Inject constructor() : ViewModel() {
    private val _filterCharactersFlow =
        MutableStateFlow(
            FilterCharacters(
                status = CharacterStatus.ALL
            )
        )

    val filterCharactersFlow: StateFlow<FilterCharacters>
        get() = _filterCharactersFlow

    fun onFilterChange(filter: FilterCharacters) {
        _filterCharactersFlow.value = filter
    }
}