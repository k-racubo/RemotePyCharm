package com.kracubo.app.ui.screens.codeeditor.projectsslist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kracubo.app.core.viewmodel.codeditor.Project
import com.kracubo.app.ui.customscrollbar.ColorType
import com.kracubo.app.ui.customscrollbar.ScrollbarConfig
import com.kracubo.app.ui.customscrollbar.rememberScrollbarState
import com.kracubo.app.ui.customscrollbar.scrollbar
import com.kracubo.app.ui.customscrollbar.verticalScrollWithScrollbar


@Composable
fun ProjectsList(toCodeEditor: () -> Unit) {

    val projects = listOf(
        Project("PP", "PythonProject1", Color(0xFF4CAF50)),
        Project("HP", "HuilaProject729", Color(0xFF26A69A)),
        Project("SD", "SalviaDivinorum", Color(0xFFBA7867)),
        Project("PH", "PinkHuetaZaborPizda", Color(0xFFD061A2)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("PP", "PythonProject1", Color(0xFF4CAF50)),
        Project("HP", "HuilaProject729", Color(0xFF26A69A)),
        Project("SD", "SalviaDivinorum", Color(0xFFBA7867)),
        Project("PH", "PinkHuetaZaborPizda", Color(0xFFD061A2)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("PP", "PythonProject1", Color(0xFF4CAF50)),
        Project("HP", "HuilaProject729", Color(0xFF26A69A)),
        Project("SD", "SalviaDivinorum", Color(0xFFBA7867)),
        Project("PH", "PinkHuetaZaborPizda", Color(0xFFD061A2)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("PP", "PythonProject1", Color(0xFF4CAF50)),
        Project("HP", "HuilaProject729", Color(0xFF26A69A)),
        Project("SD", "SalviaDivinorum", Color(0xFFBA7867)),
        Project("PH", "PinkHuetaZaborPizda", Color(0xFFD061A2)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),
        Project("CK", "CharlieKirk", Color(0xFF758DD9)),

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF22232A))
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Remote Pycharm",
                color = Color.LightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Recent projects",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(vertical = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xFF323232))
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScrollWithScrollbar(
                            rememberScrollState(),
                            rememberScrollbarState(),
                            scrollbarConfig = ScrollbarConfig(
                                padding = PaddingValues(end = 12.dp, top = 5.dp, bottom = 5.dp),
                                indicatorColor = ColorType.Solid(MaterialTheme.colorScheme.onSecondaryContainer),
                                indicatorThickness = 16.dp,
                                barThickness = 0.dp,
                                indicatorPadding = PaddingValues(2.dp),
                                barColor = ColorType.Solid(MaterialTheme.colorScheme.onSecondaryContainer)
                            ))
                ) {
                    projects.forEach { project ->
                        ProjectItem(project = project, onClick = toCodeEditor)
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Contact support",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Text(
                text = "v1.0.12",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
        }
    }
}

