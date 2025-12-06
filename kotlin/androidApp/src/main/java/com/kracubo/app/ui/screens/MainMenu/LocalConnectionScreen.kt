package com.kracubo.app.ui.screens.MainMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kracubo.app.core.ViewModel.MainMenu.LocalServerSearchScreenViewModel
import com.kracubo.app.core.ViewModel.MainMenu.SearchState

@Composable
fun LocalConnectionScreen(){
    val searchViewModel : LocalServerSearchScreenViewModel = viewModel()

    LaunchedEffect(Unit) {
        searchViewModel.startSearch()
    }
    DisposableEffect(Unit) {
        onDispose {
            searchViewModel.searchState = SearchState.SEARCHING
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (searchViewModel.searchState) {
            SearchState.SEARCHING -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 4.dp
                )
            }
            SearchState.FOUND -> {

            }
            SearchState.ERROR -> {

            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = when (searchViewModel.searchState) {
                SearchState.SEARCHING -> "Поиск сервера в локальной сети..."
                SearchState.FOUND -> "Сервер найден! Идёт подключение......"
                SearchState.ERROR -> "Ошибка: Сервер не найден в локальной сети"
            },
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (searchViewModel.searchState != SearchState.FOUND) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Назад")
            }
        }
    }
}

fun viewModel(): LocalServerSearchScreenViewModel{
    return TODO("Provide the return value")
}