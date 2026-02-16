package com.kracubo.app.core.viewmodels.codeditor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kracubo.app.core.networking.handlers.Handler

class CodeEditorViewModel : BaseViewModel() {
    var onProjectDownOnServer by mutableStateOf(false)

    init {
        Handler.setCurrentViewmodel(this)
    }

    fun onProjectClosedOnServer() { onProjectDownOnServer = true }

    override fun onCleared() {
        Handler.clearCurrentViewmodel()
    }
}