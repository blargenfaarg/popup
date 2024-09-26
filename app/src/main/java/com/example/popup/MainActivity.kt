package com.example.popup

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.popup.ui.screens.login.LoginView
import com.example.popup.di.NavigationHandler
import com.example.popup.mainui.CreateScreen
import com.example.popup.mainui.ListScreen
import com.example.popup.mainui.MapScreen
import com.example.popup.mainui.ProfileScreen
import com.example.popup.ui.screens.main.MainView
import com.example.popup.ui.screens.sign_up.GetStartedSignUpView
import com.example.popup.ui.screens.sign_up.PersonalInformationSignUpView
import com.example.popup.ui.screens.sign_up.PreferencesSelectionSignUpView
import com.example.popup.ui.screens.sign_up.SignUpViewModel
import com.example.popup.ui.theme.PopupTheme
import com.example.popup.ui.util.UiRoutes
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity.appContext = applicationContext
        setContent {
            PopupTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    navigationHandler.setNavController(navController)
                    SetUpNavHost(navController)
                }
            }
        }
    }
    companion object {
        lateinit var appContext: Context
    }
}

@Composable
fun SetUpNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = UiRoutes.LOGIN_SCREEN
    ) {
        composable(UiRoutes.LOGIN_SCREEN) {
            LoginView()
        }

        /**
         * Nested nav graph so that the sign up view model will not be cleared. Kind of works?
         */
        navigation(startDestination = UiRoutes.SIGN_UP_SCREEN_GET_STARTED, route = UiRoutes.SIGN_UP) {
            composable(UiRoutes.SIGN_UP_SCREEN_GET_STARTED) {
                val signUpViewModel: SignUpViewModel = hiltViewModel()
                GetStartedSignUpView(viewModel = signUpViewModel)
            }
            composable(UiRoutes.SIGN_UP_SCREEN_PREFERENCES) {
                val signUpViewModel: SignUpViewModel = hiltViewModel()
                PreferencesSelectionSignUpView(viewModel = signUpViewModel)
            }
            composable(UiRoutes.SIGN_UP_SCREEN_PERSONAL_INFO) {
                val signUpViewModel: SignUpViewModel = hiltViewModel()
                PersonalInformationSignUpView(viewModel = signUpViewModel)
            }
        }
        composable(UiRoutes.MAP_SCREEN) {
            MapScreen(navController)
        }
        composable(UiRoutes.LIST_SCREEN) {
            ListScreen(navController)
        }
        composable(UiRoutes.CREATE_SCREEN) {
            CreateScreen(navController)
        }
        composable(UiRoutes.PROFILE_SCREEN) {
            ProfileScreen(navController)
        }
    }
}
