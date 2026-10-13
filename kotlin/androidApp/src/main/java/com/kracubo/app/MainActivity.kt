package com.kracubo.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        val skipSplash = intent.getBooleanExtra("skipSplash", false)
        val startDestination: Any = if (skipSplash) Main else Splash

        setContent {
            val context = LocalContext.current
            AppTheme {
                Surface(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable<Splash> {
                            SplashScreen {
                                navController.navigate(Main) {
                                    popUpTo<Splash> { inclusive = true }
                                }
                            }
                        }
                        composable<Main> {
                            MainScreen(
                                onLocalScreen = { navController.navigate(LocalScreen) },
                                onRemoteScreen = { navController.navigate(RemoteScreen) }
                            )
                        }
                        composable<RemoteScreen> {
                            RemoteScreen(
                                connection = {
                                    Toast.makeText(context, "Feature in dev", Toast.LENGTH_SHORT)
                                        .show()
                                },
                                exit = {
                                    navController.navigate(Main){
                                        popUpTo<RemoteScreen> {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable<LocalScreen> {
                            LocalConnectionScreen(exitToMainScreen = {
                                navController.navigate(Main){
                                    popUpTo<LocalScreen> {
                                        inclusive = true
                                    }
                                }
                            }, toCodeEditor = {
                                context.startActivity(Intent(context, CodeEditorActivity::class.java))
                                (context as? Activity)?.finish()
                            })
                        }
                    }
                }
            }
        }
    }
}