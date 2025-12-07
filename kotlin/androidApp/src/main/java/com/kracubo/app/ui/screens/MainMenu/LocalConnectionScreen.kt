package com.kracubo.app.ui.screens.mainmenu

import android.R
import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.viewmodel.mainmenu.LocalServerSearchScreenViewModel
import com.kracubo.app.core.viewmodel.mainmenu.SearchState
import com.kracubo.app.ui.theme.AppTheme

@Composable
fun LocalConnectionScreen(viewModel: LocalServerSearchScreenViewModel = viewModel()){
    val State = viewModel.searchState
    when (State){
        SearchState.SEARCHING -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Поиск Сервера...")
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 4.dp
                    )
                    Button(onClick = {  }) {
                        Text(text = "Выйти")
                    }
                }
            }
        }
        SearchState.FOUND -> {}
        SearchState.ERROR -> {}
    }
}
