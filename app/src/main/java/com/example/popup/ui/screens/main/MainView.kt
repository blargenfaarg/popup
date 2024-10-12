package com.example.popup.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popup.mainui.CreateScreen
import com.example.popup.mainui.ListScreen
import com.example.popup.mainui.ProfileScreen
import com.example.popup.ui.screens.map.MapView
import com.example.popup.ui.util.UiRoutes

/**
 * The main view, the view that houses the bottom bar and the corresponding screens
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
@Composable
fun MainView() {
    val navController = rememberNavController()
    var selectedTab by remember {
        mutableStateOf(UiRoutes.MAP_SCREEN)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = "Map"
                        )
                    },
                    label = {
                        Text("Map")
                    },
                    selected = selectedTab == UiRoutes.MAP_SCREEN,
                    onClick = {
                        selectedTab = UiRoutes.MAP_SCREEN
                        navController.navigate(UiRoutes.MAP_SCREEN)
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "List")
                    },
                    label = {
                        Text("List")
                    },
                    selected = selectedTab == UiRoutes.LIST_SCREEN,
                    onClick = {
                        selectedTab = UiRoutes.LIST_SCREEN
                        navController.navigate(UiRoutes.LIST_SCREEN)
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Filled.AddCircle,
                            contentDescription = "Create"
                        )
                    },
                    label = {
                        Text("Create")
                    },
                    selected = selectedTab == UiRoutes.CREATE_SCREEN,
                    onClick = {
                        selectedTab = UiRoutes.CREATE_SCREEN
                        navController.navigate(UiRoutes.CREATE_SCREEN)
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile")
                    },
                    label = {
                        Text("Profile")
                    },
                    selected = selectedTab == UiRoutes.PROFILE_SCREEN,
                    onClick = {
                        selectedTab = UiRoutes.PROFILE_SCREEN
                        navController.navigate(UiRoutes.PROFILE_SCREEN)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = UiRoutes.MAP_SCREEN,
            Modifier.padding(innerPadding)
        ) {
            composable(UiRoutes.MAP_SCREEN) {
                MapView()
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
}