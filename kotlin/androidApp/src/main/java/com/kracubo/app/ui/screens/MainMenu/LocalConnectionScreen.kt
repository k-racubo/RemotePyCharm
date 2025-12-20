package com.kracubo.app.ui.screens.mainmenu

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.sp

@Composable
fun LocalConnectionScreen(
    exitToMainScreen: () -> Unit) {

    var searchPort by remember {mutableStateOf("")}

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Подключение в локальной сети",
                fontSize = 20.sp,
            )
            OutlinedTextField(
                value = searchPort,
                onValueChange = { searchPort = it },
                label = { Text( text = "Порт")} ,
                modifier = Modifier.fillMaxWidth(0.75f)
                    .graphicsLayer { clip = true },
                interactionSource = remember { MutableInteractionSource() }
            )
            Button(
                onClick = exitToMainScreen,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(text = "Подключение")
            }
            Button(
                onClick = exitToMainScreen,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(text = "Назад")
            }
        }
    }
}
