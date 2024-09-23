package com.example.popup

sealed class Screen(val route: String) {
    object mapScreen: Screen(route = "map_screen")
    object listScreen: Screen(route = "list_screen")
    object createScreen: Screen(route = "create_screen")
    object profileScreen: Screen(route = "profile_screen")
//TODO: Update and create all new screen route(if there's any).
}