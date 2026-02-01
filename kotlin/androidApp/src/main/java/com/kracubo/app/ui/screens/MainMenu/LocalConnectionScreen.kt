package com.kracubo.app.ui.screens.mainmenu

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.viewmodel.mainmenu.LocalServerSearchScreenViewModel
import com.kracubo.app.core.viewmodel.mainmenu.SearchState
import com.kracubo.app.ui.theme.ButtonBorderColor
import com.kracubo.app.ui.theme.ButtonTextColor
import com.kracubo.app.ui.theme.InputBorderColor
import com.kracubo.app.ui.theme.LabelColor
import com.kracubo.app.ui.theme.TextColor

@Composable
fun LocalConnectionScreen(exitToMainScreen: () -> Unit) {
    val context = LocalContext.current
    var searchPort by remember { mutableStateOf("") }
    val viewModel: LocalServerSearchScreenViewModel = viewModel()
    when (viewModel.searchState) {
        SearchState.ERROR -> {
            exitToMainScreen()
            Toast.makeText(context,"Connection failed", Toast.LENGTH_LONG).show()
        }
        SearchState.FOUND -> {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = {
                        exitToMainScreen()
                        viewModel.stopSearch()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Подключение в локальной сети",
                        fontSize = 20.sp,
                    )
                    Text("local server was found")
                }
            }
        }
        SearchState.QR_SEARCHING -> {

        }
        SearchState.FULL_SEARCHING -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize().blur(10.dp)
                ) {
                    OutlinedTextField(
                        value = searchPort,
                        onValueChange = { searchPort = it },
                        label = { Text("Server IP", color = LabelColor) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InputBorderColor,
                            unfocusedBorderColor = InputBorderColor,
                            focusedTextColor = TextColor,
                            unfocusedTextColor = TextColor,
                            cursorColor = TextColor,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(0.75f),
                    )
                    Spacer(Modifier.height(20.dp))
                    OutlinedButton(
                        onClick = { viewModel.startSearch() },
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ButtonBorderColor),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = ButtonTextColor
                        )
                    ) {
                        Text(text = "Подключение")
                    }
                }
                IconButton(
                    onClick = {
                        exitToMainScreen()
                        viewModel.stopSearch()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        SearchState.FULL_SEARCHING_HOLD ->{
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = {
                        exitToMainScreen()
                        viewModel.stopSearch()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = searchPort,
                        onValueChange = { searchPort = it },
                        label = { Text("Server IP", color = LabelColor) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InputBorderColor,
                            unfocusedBorderColor = InputBorderColor,
                            focusedTextColor = TextColor,
                            unfocusedTextColor = TextColor,
                            cursorColor = TextColor,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth(0.75f),
                    )
                    Spacer(Modifier.height(20.dp))
                    OutlinedButton(
                        onClick = { viewModel.startSearch() },
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, ButtonBorderColor),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = ButtonTextColor
                        )
                    ) {
                        Text(text = "Подключение")
                    }
                }
            }
        }
        SearchState.MDNS_SEARCHING -> {
            viewModel.startSearch()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        exitToMainScreen()
                        viewModel.stopSearch()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Назад",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
        SearchState.CACHE_SEARCHING -> {

        }
    }
}
