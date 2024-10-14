package com.example.popup

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.screens.login.LoginView
import com.example.popup.di.NavigationHandler
import com.example.popup.di.location.LocationHandler
import com.example.popup.ui.screens.main.MainView
import com.example.popup.ui.screens.sign_up.SignUpView
import com.example.popup.ui.theme.PopupTheme
import com.example.popup.ui.util.UiRoutes
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationHandler: NavigationHandler
    @Inject
    lateinit var locationHandler: LocationHandler

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * Permission launcher that asks for location services when the application loads
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            locationHandler.setup(fusedLocationProviderClient, appContext)
        }
    }

    /**
     * Method called to ask for location permission
     */
    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            locationHandler.setup(fusedLocationProviderClient, appContext)
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Main function called when application created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        appContext = applicationContext
        askPermissions()

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

    /**
     * From the Coil image library - setup an image cache
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}

/**
 * Setup the nav host, defining which routes go to which screens
 */
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
        composable(UiRoutes.MAIN_SCREEN) { 
            MainView()
        }
    }
}
