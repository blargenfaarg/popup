package com.example.popup.di

import androidx.navigation.NavController
import com.example.popup.ui.util.UiRoutes

/**
 * A bean that handles navigation. It is injected into main activity, at which point the nav
 * controller is provided to it
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
class NavigationHandler {

    /**
     * The nav controller to be used in order to navigate around
     */
    private lateinit var navController: NavController

    /**
     * Ensures that the nav controller is only set once
     */
    fun setNavController(navController: NavController) {
        if (::navController.isInitialized) {
            return
        }

        this.navController = navController
    }

    /**
     * Call this method to navigate to a new screen via the route provided
     */
    fun navigateToRoute(
        route: String,
        clearStack: Boolean = false
    ) {
        if (clearStack) {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = false
                    inclusive = true
                }
            }
        } else {
            navController.navigate(route)
        }
    }

    /**
     * Call this method to navigate back to the login screen. This will pop all the other
     * entries in the stack, forcing the user to log back in before they can navigate again
     */
    fun navigateBackToLoginScreen() {
        navigateToRoute(
            route = UiRoutes.LOGIN_SCREEN,
            clearStack = true
        )
    }
}