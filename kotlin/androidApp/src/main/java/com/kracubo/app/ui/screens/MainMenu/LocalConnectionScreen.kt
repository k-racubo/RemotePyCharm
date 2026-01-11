package com.kracubo.app.ui.screens.mainmenu

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.viewmodel.mainmenu.LocalServerSearchScreenViewModel
import com.kracubo.app.core.viewmodel.mainmenu.SearchState

@Composable
fun LocalConnectionScreen(exitToMainScreen: () -> Unit) {

    var searchPort by remember {mutableStateOf("")}
    val viewModel: LocalServerSearchScreenViewModel = viewModel()


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
            when(viewModel.searchState){
                SearchState.ERROR -> {
                    Text(text = "Сервер не найден")
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Ошибка",
                        modifier = Modifier.size(55.dp),
                        tint = Color.Red
                    )
                }
                SearchState.FOUND -> {
                    Text("local server was found")
                }
                SearchState.SEARCHING ->{
                    Text(text = "Поиск Сервера...")
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 4.dp
                    )
                }
                SearchState.HOLD -> {
                    OutlinedTextField(
                        value = searchPort,
                        onValueChange = { searchPort = it },
                        label = { Text( text = "Порт")} ,
                        modifier = Modifier.fillMaxWidth(0.75f)
                            .graphicsLayer { clip = true },
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    Button(
                        onClick = { viewModel.startSearch()},
                        modifier = Modifier.fillMaxWidth(0.75f)
                    ) {
                        Text(text = "Подключение")
                    }
                }
            }
            Button(
                onClick = {
                    if(viewModel.searchState == SearchState.HOLD){
                        exitToMainScreen()
                    }else{
                        viewModel.searchState = SearchState.HOLD
                    }
                },
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(text = "Назад")
            }
        }
    }
}

