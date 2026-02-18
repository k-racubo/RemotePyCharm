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
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kracubo.app.core.extensions.OnDisconnectEffect
import com.kracubo.app.core.viewmodels.codeditor.CodeEditorViewModel
import com.kracubo.app.ui.screens.codeeditor.editor.components.BottomNavigationBar
import com.kracubo.app.ui.screens.codeeditor.editor.components.CodeEditor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.IntOffset
import com.kracubo.app.core.viewmodels.codeditor.FileNode
import com.kracubo.app.core.viewmodels.codeditor.FileType

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

    var newDrawerOpen by remember { mutableStateOf(false) }

    var showTerminal by remember { mutableStateOf(false) }

    var isBlinkingTerminal by remember { mutableStateOf("■") }

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
    LaunchedEffect(showTerminal) {
        while(true){
            isBlinkingTerminal = ""
            delay(750)
            isBlinkingTerminal = "■"
            delay(750)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentFile, fontSize = 16.sp) },
                    navigationIcon = {
                        IconButton(onClick = { newDrawerOpen = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            if (viewModel.isProjectRunning) {
                                viewModel.stopProject()
                            } else
                                viewModel.runProject()
                        }) {
                            if (viewModel.isProjectRunning) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Stop",
                                    tint = Color.Red,
                                    modifier = Modifier.size(28.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Run",
                                    tint = Color(0xFF4CAF50),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
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

        NewProjectDrawer(
            isOpen = newDrawerOpen,
            vm = viewModel,
            onClose = { newDrawerOpen = false },
            onFileSelected = { fileName ->
                currentFile = fileName
                newDrawerOpen = false
            }
        )

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
                Column(Modifier.fillMaxWidth().height(25.dp).padding(top = 1.dp).clickable(onClick = {showTerminal = !showTerminal})) {
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
                }
                Spacer(Modifier.height(4.dp))
                Column(Modifier.fillMaxWidth().fillMaxHeight(0.80f).padding(top = 31.dp)){
                    Text(
                        text = "(.venv) user@Users-MacBook-Air\nProjectFolder\n% ",
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                    Text(
                        text = isBlinkingTerminal,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewProjectDrawer(
    isOpen: Boolean,
    vm: CodeEditorViewModel,
    onClose: () -> Unit,
    onFileSelected: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var offsetX by remember { mutableStateOf(0f) }

    LaunchedEffect(isOpen) {
        if (isOpen) offsetX = 0f
        else offsetX = -300f
    }

    AnimatedVisibility(
        visible = isOpen,
        enter = fadeIn(animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val alpha = ((300f + offsetX) / 300f).coerceIn(0.5f, 1f) * 0.5f

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = alpha))
                    .padding(start = 300.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 300.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX < -150f) {
                                    scope.launch {
                                        animate(
                                            initialValue = offsetX,
                                            targetValue = -300f,
                                            animationSpec = tween(150)
                                        ) { value, _ -> offsetX = value }
                                        onClose()
                                    }
                                } else {
                                    scope.launch {
                                        animate(
                                            initialValue = offsetX,
                                            targetValue = 0f,
                                            animationSpec = tween(150)
                                        ) { value, _ -> offsetX = value }
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceIn(-300f, 0f)
                        }
                    }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        scope.launch {
                            animate(
                                initialValue = offsetX,
                                targetValue = -300f,
                                animationSpec = tween(150)
                            ) { value, _ -> offsetX = value }
                            onClose()
                        }
                    }
            )

            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .align(Alignment.CenterStart)
                    .offset { IntOffset(offsetX.toInt(), 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX < -150f) {
                                    scope.launch {
                                        animate(
                                            initialValue = offsetX,
                                            targetValue = -300f,
                                            animationSpec = tween(150)
                                        ) { value, _ -> offsetX = value }
                                        onClose()
                                    }
                                } else {
                                    scope.launch {
                                        animate(
                                            initialValue = offsetX,
                                            targetValue = 0f,
                                            animationSpec = tween(150)
                                        ) { value, _ -> offsetX = value }
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceIn(-300f, 0f)
                        }
                    },
                color = Color(0xFF2D2D30),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Project Files", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }

                    Divider(color = Color.White.copy(alpha = 0.2f))

                    val projectTree by vm.projectTree.collectAsState()

                    projectTree?.let {
                        FileTreeView(
                            node = it,
                            onFileSelected = { filePath ->
                                onFileSelected(filePath)
                                onClose()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FileTreeView(
    node: FileNode,
    level: Int = 0,
    onFileSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(level == 0) }

    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures { }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = (level * 20).dp)
                .clickable {
                    when (node.type) {
                        FileType.FOLDER -> expanded = !expanded
                        FileType.FILE -> onFileSelected(node.path)
                    }
                }
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Icon(
                imageVector = when (node.type) {
                    FileType.FOLDER -> if (expanded) Icons.Default.PlayArrow else Icons.Default.PlayArrow
                    FileType.FILE -> Icons.Default.Menu
                },
                contentDescription = null,
                tint = when (node.type) {
                    FileType.FOLDER -> Color(0xFF64B5F6)
                    FileType.FILE -> Color.White.copy(alpha = 0.7f)
                },
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = node.name,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }

        if (expanded && node.type == FileType.FOLDER && !node.children.isNullOrEmpty()) {
            node.children.forEach { child ->
                FileTreeView(
                    node = child,
                    level = level + 1,
                    onFileSelected = onFileSelected
                )
            }
        }
    }
}