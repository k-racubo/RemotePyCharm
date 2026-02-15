package com.kracubo.app.ui.screens.codeeditor.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTopBar(
    fileName: String,
    onSearchClick: () -> Unit = {},
    onRunClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Логотип Kotlin (синяя буква K)
                Text(
                    text = "K",
                    color = Color(0xFF7F52FF), // Синий цвет Kotlin
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(end = 8.dp)
                )
                // Имя файла с выпадающим меню
                Text(
                    text = fileName,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
                IconButton(
                    onClick = { /* TODO: File dropdown */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "File menu",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
            IconButton(onClick = onRunClick) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Run",
                    tint = Color(0xFF4ADE80) // Зеленый цвет для кнопки Run
                )
            }
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }
        }
    )
}

