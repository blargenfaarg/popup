package com.example.popup.ui.screens.login

import com.example.popup.ui.util.ViewModelEvent

/**
 * The events that can occur in the login screen
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
sealed class LoginViewEvent: ViewModelEvent {
    data object OnLoginClicked: LoginViewEvent()
    data class OnUsernameChange(val username: String): LoginViewEvent()
    data class OnPasswordChange(val password: String): LoginViewEvent()
    data object OnCreateAccountClicked: LoginViewEvent()
}