package com.kracubo.app.core.ViewModel.MainMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocalServerSearchScreenViewModel : ViewModel() {
    var searchState by mutableStateOf<SearchState>(SearchState.SEARCHING)
    private var searchJob: Job? = null

    fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchState = SearchState.SEARCHING  //в будущем добавить условия нахождения и ошибки
        }                                        // пока что будет вечная загрузка
    }
}
enum class  SearchState {
    SEARCHING,
    FOUND,
    ERROR
}