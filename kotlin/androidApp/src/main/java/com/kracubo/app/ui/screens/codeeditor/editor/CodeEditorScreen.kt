package com.kracubo.app.ui.screens.codeeditor.editor

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.extenssions.OnDisconnectEffect
import com.kracubo.app.core.viewmodels.codeditor.CodeEditorViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorScreen(
    onNavigateToMainMenu: () -> Unit
) {
    val viewModel: CodeEditorViewModel = viewModel()

    LaunchedEffect(viewModel.onProjectDownOnServer) {
        if (viewModel.onProjectDownOnServer) {
            viewModel.onProjectDownOnServer = false
            onNavigateToMainMenu()
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
                BottomNavigationBar(
                    onTerminalClick = { showTerminal = !showTerminal },
                    onSearchClick = { /* poisk huini */ }
                )
            }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                CodeEditor(
                    modifier = Modifier
                        .weight(if (showTerminal) 1f else 1f)
                        .fillMaxWidth(),
                    code = codeContent,
                    onCodeChange = { codeContent = it }
                )

                if (showTerminal) {
                    TerminalView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.35f)
                    )
                }
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

@Composable
fun TerminalView(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier,
        color = Color(0xFF1E1E1E)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(scrollState)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "(.venv) ",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50), // Green for venv
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "user@Users-MacBook-Air",
                    fontSize = 12.sp,
                    color = Color(0xFF858585),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ProjectFolder",
                    fontSize = 12.sp,
                    color = Color(0xFF64B5F6), // Light blue for folder
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "% ",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "▋",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onTerminalClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clickable(onClick = onTerminalClick)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = ">",
                        color = Color(0xFF2B2B2B),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "_",
                        color = Color(0xFF2B2B2B),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            IconButton(onClick = onSearchClick) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF2B2B2B)
                )
            }
        }
    }
}
// бля ебаный секс нахуй меня ы очко будто тра=алли, мб нахуй переписать опять все но вроде норм
//надл доработать дизайн и не только