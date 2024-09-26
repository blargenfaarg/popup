package com.example.popup.ui.util

/**
 * The basic UiEvents that view models can sends to their views.
 *
 * PopBackStack -> An event that asks to move back a screen
 * Navigate -> An event that asks to navigate to the supplied route
 * ShowError -> An event that asks to show an error dialog on the screen with the supplied title
 *              and message
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
sealed class UiEvent {
    data object PopBackStack: UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowError(val title: String, val message: String): UiEvent()
}