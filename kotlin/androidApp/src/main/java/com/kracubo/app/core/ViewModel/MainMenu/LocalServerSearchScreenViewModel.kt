package com.kracubo.app.core.viewmodel.mainmenu

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kracubo.app.core.nsdManager.NsdHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LocalServerSearchScreenViewModel(application: Application) : AndroidViewModel(application) {
    var searchState by mutableStateOf<SearchState>(SearchState.HOLD)
    private var searchJob: Job? = null
    private val appContext get() = getApplication<Application>().applicationContext
    fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchState = SearchState.SEARCHING
            val nsdHelper = NsdHelper(appContext)
            nsdHelper.discoverServices()
        }
    }
    fun stopSearch(){
        searchJob?.cancel()
        searchState = SearchState.HOLD
    }
    fun errorSearch(){
        searchJob?.cancel()
        searchState = SearchState.ERROR
    }
}
