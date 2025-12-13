package com.kracubo.app.ui.screens.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(OnLocalScreen: () -> Unit,
               OnRemoteScreen: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Выберите способ подключения",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = { OnLocalScreen() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Локальное подключение")
            }
            Button(
                onClick = { OnRemoteScreen() }
            ) {
                Text("Удаленное подключение")
            }
        }
    }
}