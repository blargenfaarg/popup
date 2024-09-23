package com.example.popup

sealed class Screen(val route: String) {
    data object MapScreen: Screen(route = "map_screen")
    data object ListScreen: Screen(route = "list_screen")
    data object CreateScreen: Screen(route = "create_screen")
    data object ProfileScreen: Screen(route = "profile_screen")
//TODO: Update and create all new screen route(if there's any).
}