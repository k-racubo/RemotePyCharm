package com.kracubo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kracubo.app.ui.screens.mainmenu.LocalConnectionScreen
import com.kracubo.app.ui.screens.mainmenu.MainScreen
import com.kracubo.app.ui.screens.mainmenu.RemoteScreen
import com.kracubo.app.ui.screens.mainmenu.SplashScreen
import com.kracubo.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContent {
            AppTheme(darkTheme = true){
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
                                OnLocalScreen = { navController.navigate("LocalScreen")},
                                OnRemoteScreen = {navController.navigate("RemoteScreen") }
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
                            LocalConnectionScreen(CancelServerSearch = {
                                navController.navigate("main"){
                                    popUpTo("LocalScreen"){
                                        inclusive = true
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}