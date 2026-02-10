package com.kracubo.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kracubo.app.ui.screens.mainmenu.RemoteScreen
import com.kracubo.app.ui.screens.mainmenu.LocalConnectionScreen
import com.kracubo.app.ui.screens.mainmenu.MainScreen
import com.kracubo.app.ui.screens.mainmenu.SplashScreen
import com.kracubo.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent {
            val context = LocalContext.current
            AppTheme(){
                Surface(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen {
                                navController.navigate("main") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }
                        composable("main") {
                            MainScreen(
                                onLocalScreen = { navController.navigate("LocalScreen")},
                                onRemoteScreen = {navController.navigate("RemoteScreen") }
                            )
                        }
                        composable("RemoteScreen") {
                            RemoteScreen(
                                Connection = {},
                                Exit = {
                                    navController.navigate("main"){
                                        popUpTo("RemoteScreen"){
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable("LocalScreen"){
                            LocalConnectionScreen(exitToMainScreen = {
                                navController.navigate("main"){
                                    popUpTo("LocalScreen"){
                                        inclusive = true
                                    }
                                }
                            }, toCodeEditor = {
                                val intent = Intent(context, CodeEditorActivity::class.java)
                                context.startActivity(intent)
                            })
                        }
                    }
                }
            }
        }
    }
}