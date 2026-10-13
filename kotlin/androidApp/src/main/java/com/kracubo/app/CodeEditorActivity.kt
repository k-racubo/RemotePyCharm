package com.kracubo.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kracubo.app.ui.theme.AppTheme
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kracubo.app.ui.screens.codeeditor.editor.CodeEditorScreen
import com.kracubo.app.ui.screens.codeeditor.projectsslist.ProjectsList


class CodeEditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            var showExitDialog by remember { mutableStateOf(false) }

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            BackHandler(
                enabled = currentDestination?.hasRoute<ProjectsList>() == true
            ) {
                showExitDialog = true
            }

            AppTheme {
                Surface(Modifier.fillMaxSize()) {
                    NavHost(navController = navController, startDestination = ProjectsList) {
                        composable<ProjectsList> {
                            ProjectsList(
                                { navController.navigate(CodeEditor) },
                                { showExitDialog = true }
                            )
                        }
                        composable<CodeEditor> {
                            CodeEditorScreen(
                                onNavigateToMainMenu = {
                                    Toast.makeText(context, "Project was closed in plugin", Toast.LENGTH_SHORT).show()

                                    returnToMain()
                                },
                                navigateToProjectsListScreen = {
                                    navController.navigate(ProjectsList) {
                                        popUpTo<CodeEditor> {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                    if (showExitDialog) {
                        AlertDialog(
                            onDismissRequest = { showExitDialog = false },
                            title = { Text("Disconnect?") },
                            text = { Text("Close current session and return to main menu?") },
                            confirmButton = {
                                TextButton(onClick = { returnToMain() }) { Text("Yes") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showExitDialog = false }) { Text("No") }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun returnToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("skipSplash", true)
        }
        startActivity(intent)
        finish()
    }
}