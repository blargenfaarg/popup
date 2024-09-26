package com.example.popup.mainui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.popup.ui.util.UiRoutes.CREATE_SCREEN
import com.example.popup.ui.util.UiRoutes.LIST_SCREEN
import com.example.popup.ui.util.UiRoutes.MAP_SCREEN
import com.example.popup.ui.util.UiRoutes.PROFILE_SCREEN

@Composable
fun LoadNavBar(navController : NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Place, contentDescription = "Map") },
            label = { Text("Map") },
            selected = false,
            onClick = {navController.navigate(MAP_SCREEN) } //Nav to other screens
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "List") },
            label = { Text("List") },
            selected = false,
            onClick = {navController.navigate(LIST_SCREEN) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = "Create") },
            label = { Text("Create") },
            selected = false,
            onClick = {navController.navigate(CREATE_SCREEN) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = {navController.navigate(PROFILE_SCREEN) }
        )

    }
}