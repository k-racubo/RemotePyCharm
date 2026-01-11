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

    private var nsdHelper: NsdHelper? = null

    fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchState = SearchState.SEARCHING

            nsdHelper = NsdHelper(appContext).apply {
                listener = object : NsdHelper.DiscoveryListener {
                    override fun onServiceFound() {
                        viewModelScope.launch {
                            searchState = SearchState.FOUND
                        }
                    }

                    override fun onError() {
                        viewModelScope.launch {
                            searchState = SearchState.ERROR
                        }
                    }
                }
            }
            nsdHelper?.discoverServices()
        }
    }

    fun stopSearch() {
        searchJob?.cancel()
        searchState = SearchState.HOLD
        nsdHelper = null
    }

    fun errorSearch() {
        searchJob?.cancel()
        searchState = SearchState.ERROR
    }
}

