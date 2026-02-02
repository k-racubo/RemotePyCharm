package com.kracubo.app.core.viewmodel.mainmenu

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kracubo.app.core.nsdManager.NsdHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LocalServerSearchScreenViewModel(application: Application) : AndroidViewModel(application) {
    var searchState by mutableStateOf<SearchState>(SearchState.MDNS_SEARCHING) // MDNS_SEARCHING
    private var searchJob: Job? = null
    private val appContext get() = getApplication<Application>().applicationContext

    private var nsdHelper: NsdHelper? = null

    companion object {
        private const val KEY_CACHE_LOCAL_SEARCH_PORT = "LOCAL_SEARCH_PORT"
        private const val KEY_CACHE_LOCAL_SEARCH_IP = "LOCAL_SEARCH_IP"
    }


    fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchState = SearchState.MDNS_SEARCHING
            nsdHelper = NsdHelper(appContext).apply {
                listener = object : NsdHelper.DiscoveryListener {
                    override fun onServiceFound() {
                        searchJob?.cancel()  //  <---
                        nsdHelper = null     //  <---

                        viewModelScope.launch {
                            searchState = SearchState.FOUND
                        }
                    }
                    override fun onError() {
                        viewModelScope.launch {
                            cacheSearching() // <---
                        }
                    }
                }
            }
            nsdHelper?.discoverServices()
            val result = withTimeoutOrNull(4000L) {
                while (searchState == SearchState.MDNS_SEARCHING) {
                    delay(100)
                }
            }
            if (result == null && searchState == SearchState.MDNS_SEARCHING) {
                searchState = SearchState.CACHE_SEARCHING
                cacheSearching()
            }
        }
    }
    fun startFullSearch(){
        searchState = SearchState.FULL_SEARCHING
    }
    fun stopSearch() {
        searchJob?.cancel()
        nsdHelper = null
    }
    fun cacheSearching() {
        viewModelScope.launch {
            val preferences: SharedPreferences = appContext.getSharedPreferences("", Context.MODE_PRIVATE)
            val cachedPort = preferences.getInt(KEY_CACHE_LOCAL_SEARCH_PORT, -1)
            val cachedIp = preferences.getString(KEY_CACHE_LOCAL_SEARCH_IP, null)
            if (cachedPort != -1 && cachedIp != null) {
                searchState = SearchState.FOUND
            } else {
                delay(1000)
                searchState = SearchState.FULL_SEARCHING_HOLD
            }
        }
    }
    fun errorSearch() {
        searchJob?.cancel()
        searchState = SearchState.ERROR
        nsdHelper = null // <---
    }
}

