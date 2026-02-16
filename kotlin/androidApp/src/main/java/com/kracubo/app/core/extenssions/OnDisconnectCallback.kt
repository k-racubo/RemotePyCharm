package com.kracubo.app.core.extenssions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.kracubo.app.core.viewmodels.codeditor.BaseViewModel

@Composable
fun <T : BaseViewModel> T.OnDisconnectEffect(
    onDisconnect: () -> Unit
) {
    LaunchedEffect(this.onDisconnect) {
        if (this@OnDisconnectEffect.onDisconnect) {
            this@OnDisconnectEffect.onDisconnect = false
            onDisconnect()
        }
    }
}