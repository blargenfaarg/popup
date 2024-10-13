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
import com.example.popup.mainui.CreateMiscScreen
import com.example.popup.mainui.CreateNameScreen
import com.example.popup.mainui.CreatePlaceScreen
import com.example.popup.mainui.CreateReviewScreen
import com.example.popup.mainui.CreateScreen
import com.example.popup.mainui.CreateTimeScreen
import com.example.popup.mainui.CreateTypeScreen
import com.example.popup.mainui.ListScreen
import com.example.popup.mainui.MapScreen
import com.example.popup.mainui.ProfileScreen
import com.example.popup.ui.screens.main.MainView
import com.example.popup.ui.screens.sign_up.GetStartedSignUpView
import com.example.popup.ui.screens.sign_up.PersonalInformationSignUpView
import com.example.popup.ui.screens.sign_up.PreferencesSelectionSignUpView
import com.example.popup.ui.screens.sign_up.SignUpView
import com.example.popup.ui.screens.sign_up.SignUpViewModel
import com.example.popup.ui.theme.PopupTheme
import com.example.popup.ui.util.UiRoutes
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = applicationContext

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
        composable(UiRoutes.SIGN_UP) {
            SignUpView()
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
        composable(UiRoutes.CREATE_NAME_SCREEN) {
            CreateNameScreen(navController)
        }
        composable(UiRoutes.CREATE_TYPE_SCREEN) {
            CreateTypeScreen(navController)
        }
        composable(UiRoutes.CREATE_TIME_SCREEN) {
            CreateTimeScreen(navController)
        }
        composable(UiRoutes.CREATE_PLACE_SCREEN) {
            CreatePlaceScreen(navController)
        }

        composable(UiRoutes.CREATE_MISC_SCREEN) {
            CreateMiscScreen(navController)
        }
        composable(UiRoutes.CREATE_REVIEW_SCREEN) {
            CreateReviewScreen(navController)
        }


    }
}
