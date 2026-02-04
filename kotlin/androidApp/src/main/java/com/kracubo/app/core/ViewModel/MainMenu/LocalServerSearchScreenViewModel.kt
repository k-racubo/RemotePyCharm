package com.kracubo.app.core.viewmodel.mainmenu

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kracubo.app.core.nsdManager.NsdHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocalServerSearchScreenViewModel(application: Application) : AndroidViewModel(application) {
    var searchState by mutableStateOf(SearchState.MDNS_SEARCHING)
        private set

    private var searchJob: Job? = null
    private val appContext get() = getApplication<Application>().applicationContext

    private var nsdHelper: NsdHelper? = null

    private val prefs by lazy { appContext.getSharedPreferences("local_search_prefs", Context.MODE_PRIVATE) }

    private val KEY_CACHE_LOCAL_SEARCH_IP = "LOCAL_SEARCH_IP"
    private val KEY_CACHE_LOCAL_SEARCH_PORT = "LOCAL_SEARCH_PORT"

    init {
        nsdHelper = NsdHelper(appContext).apply {
            listener = object : NsdHelper.DiscoveryListener {
                override fun onServiceFound(ip: String, port: Int) {
                    viewModelScope.launch(Dispatchers.Main) {
                        searchState = SearchState.FOUND
                        stopSearch()
                    }
                }

                override fun onError() { Log.e("Nsd Helper", "error") }
            }
        }

        startSearch()
    }

    fun startSearch() {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            searchState = SearchState.MDNS_SEARCHING

            nsdHelper?.discoverService()

            delay(3500)

            if (searchState == SearchState.MDNS_SEARCHING) {
                nsdHelper?.stopDiscovery()

                searchState = SearchState.CACHE_SEARCHING
                cacheSearching()

                Log.i("NsdViewModel", "Тайм-аут: сервис не найден")
            }
        }
    }

    fun startFullSearch(){
        searchState = SearchState.MANUAL_CONNECTING //<----
    } // дописать логику прямого подключения с searchJob и delay на время подключения иначе финальный ERROR

    fun stopSearch() {
        nsdHelper?.stopDiscovery()
        searchJob?.cancel()
    }

    fun cacheSearching() {
        searchJob = viewModelScope.launch {
            delay(1500)

            val cachedIp = prefs.getString(KEY_CACHE_LOCAL_SEARCH_IP, null)

            val cachedPort = prefs.getInt(KEY_CACHE_LOCAL_SEARCH_PORT, -1)

            if(cachedPort != -1 && cachedIp != null){
                // тут затычка на переключение на следущий способ (с ручным вводом) т.к. кэш есть но логики нет еще на проверку <-----
                searchState = SearchState.MANUAL_INPUT

            }
            else{
                searchState = SearchState.MANUAL_INPUT
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopSearch()
        nsdHelper = null
    }
}

