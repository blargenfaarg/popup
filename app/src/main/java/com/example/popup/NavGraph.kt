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
fun SetupNavGragh(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.mapScreen.route
    ){
        composable(
            route = Screen.mapScreen.route
        ) {
            MapScreen(navController)
        }
        composable(
            route = Screen.listScreen.route
        ) {
            ListScreen(navController)
        }
        composable(
            route = Screen.createScreen.route
        ) {
            CreateScreen(navController)
        }
        composable(
            route = Screen.profileScreen.route
        ) {
            ProfileScreen(navController)
        }
    }
}