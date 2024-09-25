package com.example.popup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.screens.login.LoginView
import com.example.popup.ui.screens.main.MainView
import com.example.popup.ui.screens.sign_up.SignUpView
import com.example.popup.ui.theme.PopupTheme
import com.example.popup.ui.util.UiRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = UiRoutes.LOGIN_SCREEN
                    ) {
                        composable(UiRoutes.LOGIN_SCREEN) {
                            LoginView(
                                onNavigate = {
                                    navController.navigate(it.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = false
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        composable(UiRoutes.SIGN_UP_SCREEN) {
                            SignUpView(
                                onNavigate = {

                                }
                            )
                        }
                        composable(UiRoutes.MAIN_SCREEN) {
                            MainView()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PopupTheme {
        Greeting("Android")
    }
}