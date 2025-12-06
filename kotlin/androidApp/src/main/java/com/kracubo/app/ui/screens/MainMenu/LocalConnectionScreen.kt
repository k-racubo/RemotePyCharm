package com.kracubo.app.ui.screens.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kracubo.app.core.viewmodel.mainmenu.LocalServerSearchScreenViewModel
import com.kracubo.app.core.viewmodel.mainmenu.SearchState

@Composable
fun LocalConnectionScreen(){
    var StateConnection by remember { mutableStateOf(SearchState.SEARCHING) }

}