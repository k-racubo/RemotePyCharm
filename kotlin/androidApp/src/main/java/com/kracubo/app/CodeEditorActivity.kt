package com.kracubo.app

import android.os.Bundle
import android.widget.Toast
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
                            Toast.makeText(context,"this is projects list", Toast.LENGTH_LONG).show()
                        }
                        composable("CodeEditor") {

                        }
                    }
                }
            }
        }
    }
}