package com.example.popup

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.popup.mainui.CreateScreen
import com.example.popup.mainui.ListScreen
import com.example.popup.mainui.MapScreen
import com.example.popup.mainui.ProfileScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.MapScreen.route
    ){
        composable(
            route = Screen.MapScreen.route
        ) {
            MapScreen(navController)
        }
        composable(
            route = Screen.ListScreen.route
        ) {
            ListScreen(navController)
        }
        composable(
            route = Screen.CreateScreen.route
        ) {
            CreateScreen(navController)
        }
        composable(
            route = Screen.ProfileScreen.route
        ) {
            ProfileScreen(navController)
        }
    }
}