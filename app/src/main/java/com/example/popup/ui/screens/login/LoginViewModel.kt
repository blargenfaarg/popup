package com.example.popup.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.popup.di.NavigationHandler
import com.example.popup.model.request.user.LoginUserRequest
import com.example.popup.networking.api.IApiService
import com.example.popup.ui.util.APopUpViewModel
import com.example.popup.ui.util.UiEvent
import com.example.popup.ui.util.UiRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model to manage the login activity
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: IApiService,
    private val navigationHandler: NavigationHandler
): APopUpViewModel<LoginViewEvent>() {

    companion object {
        private val LOGIN_ERROR_EVENT = UiEvent.ShowError(
            title = "Login Unsuccessful",
            message = "Unable to complete login. Double check your username or password and try again"
        )
    }

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    private var _loading = MutableStateFlow(false)
    var loading = _loading.asStateFlow()

    /**
     * Receive an event from the UI and decide what to do with it
     */
    override fun onEvent(event: LoginViewEvent) {
        when (event) {
            is LoginViewEvent.OnLoginClicked -> attemptLoginUser()
            is LoginViewEvent.OnSignUpClicked -> {
                navigationHandler.navigateToRoute(UiRoutes.SIGN_UP)
            }
            is LoginViewEvent.OnPasswordChange -> password = event.password
            is LoginViewEvent.OnUsernameChange -> username = event.username
        }
    }

    /**
     * Attempt to log in the user. Sends a call to the api service in a coroutine and if the
     * login was successful, sends event to move to the next screen, otherwise shows an error
     */
    private fun attemptLoginUser() {
        _loading.value = true

        viewModelScope.launch {
            try {
                val response = apiService.loginUser(request = LoginUserRequest(username, password))
                when (response.wasSuccessful()) {
                    true -> navigationHandler.navigateToRoute(UiRoutes.MAIN_SCREEN, true)
                    false -> sendUiEventToChannel(LOGIN_ERROR_EVENT)
                }
            } finally {
                _loading.value = false
            }
        }
    }
}