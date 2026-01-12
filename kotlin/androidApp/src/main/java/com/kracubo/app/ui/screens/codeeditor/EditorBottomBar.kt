package com.kracubo.app.ui.screens.codeeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditorBottomBar(
    onInsert: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CodeColors.BottomBarBackground)
    ) {
        // Первая строка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditorKey("KEYS", {})
            EditorKey(
                label = "Run",
                onClick = {},
                icon = Icons.Default.PlayArrow,
                iconColor = Color(0xFF4ADE80),
                containerColor = Color(0xFF4ADE80)
            )
            EditorKey(
                label = "4",
                onClick = {},
                icon = Icons.Default.Menu,
                containerColor = CodeColors.ButtonBackground
            )
            EditorKey("<>", onClick = {})
            EditorKey("{}", onClick = {})
            EditorKey("Kotlin", onClick = {}, containerColor = Color(0xFF7F52FF))
            EditorKey("Coroutines", onClick = {})
            Text(
                text = "4 spaces",
                color = Color.Gray,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                text = "12:8",
                color = Color.Gray,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                text = "UTF-8 LF",
                color = Color.Gray,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
        
        // Вторая строка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Undo",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
            EditorKey(
                label = "Run",
                onClick = {},
                icon = Icons.Default.PlayArrow,
                iconColor = Color(0xFF4ADE80),
                containerColor = Color(0xFF4ADE80)
            )
            EditorKey(
                label = "4",
                onClick = {},
                icon = Icons.Default.Delete,
                iconColor = Color(0xFF4ADE80),
                containerColor = Color(0xFF4ADE80)
            )
            EditorKey("<>", {})
            EditorKey("{}", {})
        }
    }
}
@Composable
fun EditorKey(
    label: String,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconColor: Color = Color.White,
    containerColor: Color = CodeColors.ButtonBackground
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp)
            )
            if (label.isNotEmpty()) {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(4.dp))
            }
        }
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = if (containerColor == CodeColors.ButtonBackground) Color.White else Color.Black
            )
        }
    }
}

