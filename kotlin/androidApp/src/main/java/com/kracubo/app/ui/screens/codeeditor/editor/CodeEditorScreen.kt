package com.kracubo.app.ui.screens.codeeditor.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.extensions.OnDisconnectEffect
import com.kracubo.app.core.viewmodels.codeditor.CodeEditorViewModel
import com.kracubo.app.ui.screens.codeeditor.editor.components.BottomNavigationBar
import com.kracubo.app.ui.screens.codeeditor.editor.components.CodeEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    onNavigateToMainMenu: () -> Unit,
    navigateToProjectsListScreen: () -> Unit
) {
    val viewModel: CodeEditorViewModel = viewModel()

    viewModel.OnDisconnectEffect(onNavigateToMainMenu)

    LaunchedEffect(viewModel.onProjectDownOnServer) {
        if (viewModel.onProjectDownOnServer) {
            viewModel.onProjectDownOnServer = false
            navigateToProjectsListScreen()
        }
    }

    var drawerOpen by remember { mutableStateOf(false) }

    var showTerminal by remember { mutableStateOf(false) }

    var currentFile by remember { mutableStateOf("python_file.py") }

    var codeContent by remember { mutableStateOf(
        """import asyncio

async def factorial(name, number):
    f = 1
    for i in range(2, number + 1):
        print("Nu i huynya eto")
        await asyncio.sleep(1)
        f *= i
        print("Zaebalo pisat etu pizdu")
    return f
"""
    ) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentFile, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = { drawerOpen = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* ebani sex suka */ }) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Run",
                                tint = Color(0xFF4CAF50), // Green color for play button
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF2B2B2B),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BottomNavigationBar(
                        onTerminalClick = { showTerminal = !showTerminal },
                        onSearchClick = { /* poisk huini */ }
                    )
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize()) {
                CodeEditor(
                    modifier = Modifier
                        .weight(if (showTerminal) 1f else 1f)
                        .fillMaxWidth()
                        .padding(top = paddingValues.calculateTopPadding()),
                    code = codeContent,
                    onCodeChange = { codeContent = it }
                )

            }
        }

        AnimatedVisibility(
            visible = drawerOpen,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .clickable { drawerOpen = false }
            ) {
                Surface(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    color = Color(0xFF2D2D30),
                    shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                ) {
                    FileTreeDrawerContent(
                        onFileSelected = { fileName ->
                            currentFile = fileName
                            drawerOpen = false
                        },
                        onDismiss = { drawerOpen = false }
                    )
                }
            }
        }

        if(showTerminal){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFF242425))
                    .align(Alignment.BottomCenter)
            ) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Column(Modifier.fillMaxWidth().fillMaxHeight(0.15f).padding(top = 1.dp).clickable(onClick = {showTerminal = !showTerminal})) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow Down",
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "(.venv) user@Users-MacBook-Air\nProjectFolder\n% ",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FileTreeDrawerContent(
    onFileSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D2D30))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PythonProject1",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FileTree(
            onFileSelected = onFileSelected
        )
    }
}

@Composable
fun FileTree(onFileSelected: (String) -> Unit) {
    var expandedFolders by remember { mutableStateOf(setOf("ProjectFolder")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        FileTreeItem(
            name = "ProjectFolder",
            isFolder = true,
            isExpanded = expandedFolders.contains("ProjectFolder"),
            onClick = {
                expandedFolders = if (expandedFolders.contains("ProjectFolder")) {
                    expandedFolders - "ProjectFolder"
                } else {
                    expandedFolders + "ProjectFolder"
                }
            }
        )

        if (expandedFolders.contains("ProjectFolder")) {
            Spacer(modifier = Modifier.height(4.dp))
            FileTreeItem(
                name = "UsableFolder",
                isFolder = true,
                isExpanded = expandedFolders.contains("UsableFolder"),
                indent = 1,
                onClick = {
                    expandedFolders = if (expandedFolders.contains("UsableFolder")) {
                        expandedFolders - "UsableFolder"
                    } else {
                        expandedFolders + "UsableFolder"
                    }
                }
            )

            if (expandedFolders.contains("UsableFolder")) {
                Spacer(modifier = Modifier.height(4.dp))
                FileTreeItem(
                    name = "python_file.py",
                    isFolder = false,
                    indent = 2,
                    onClick = { onFileSelected("python_file.py") }
                )
                FileTreeItem(
                    name = "huila_file.py",
                    isFolder = false,
                    indent = 2,
                    onClick = { onFileSelected("huila_file.py") }
                )
                FileTreeItem(
                    name = "FolderHui",
                    isFolder = true,
                    isExpanded = false,
                    indent = 2,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            FileTreeItem(
                name = "UnusableFolder",
                isFolder = true,
                isExpanded = false,
                indent = 1,
                onClick = { }
            )
        }
    }
}

@Composable
fun FileTreeItem(
     name: String,
    isFolder: Boolean,
    isExpanded: Boolean = false,
    indent: Int = 0,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (indent * 16).dp)
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // хАХАХ ебучие смайлики не хотите? а мне пох вид имеют
        Text(
            text = if (isFolder) {
                if (isExpanded) "📂" else "📁"
            } else {
                "📄"
            },
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

