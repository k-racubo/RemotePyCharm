package com.kracubo.app.core.viewmodels.codeditor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    var onDisconnect by mutableStateOf(false)

    fun onDisconnect() { onDisconnect = true }
}