package com.kracubo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kracubo.app.ui.theme.AppTheme
import androidx.navigation.compose.composable
import com.kracubo.app.ui.screens.codeeditor.editor.CodeEditorScreen
import com.kracubo.app.ui.screens.codeeditor.projectsslist.ProjectsList


class CodeEditorActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent {
            val context = LocalContext.current
            AppTheme() {
                Surface(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "ListProjects") {
                        composable("ListProjects"){
                            ProjectsList { navController.navigate("CodeEditor") }
                        }
                        composable("CodeEditor") {
                            CodeEditorScreen()
                        }
                    }
                }
            }
        }
    }
}