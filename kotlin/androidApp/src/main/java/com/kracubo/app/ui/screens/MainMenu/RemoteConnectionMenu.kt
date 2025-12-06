package com.kracubo.app.ui.screens.mainmenu

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun RemoteScreen(Connection: () -> Unit){
    var serverIp by remember { mutableStateOf("") }
    var serverPort by remember { mutableStateOf("") }
    var authToken by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = serverIp,
                onValueChange = { serverIp = it },
                label = { Text("IP адрес сервера") },
                modifier = Modifier.fillMaxWidth(0.75f).padding(bottom = 14.dp)
                    .graphicsLayer { clip = true },
                interactionSource = remember { MutableInteractionSource() }
            )
            OutlinedTextField(
                value = serverPort,
                onValueChange = { serverPort = it },
                label = { Text("Порт") },
                modifier = Modifier.fillMaxWidth(0.75f).padding(bottom = 14.dp)
                    .graphicsLayer { clip = true },
                interactionSource = remember { MutableInteractionSource() }
            )
            OutlinedTextField(
                value = authToken,
                onValueChange = { authToken = it },
                label = { Text("Токен авторизации") },
                modifier = Modifier.fillMaxWidth(0.75f).padding(bottom = 14.dp)
                    .graphicsLayer { clip = true },
                interactionSource = remember { MutableInteractionSource() }
            )
            Button(onClick = { Connection() }) {
                Text("Подключение")
            }
        }
    }
}