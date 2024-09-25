package com.example.popup.ui.screens.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.model.request.user.CreateUserRequest
import com.example.popup.networking.api.IApiService
import com.example.popup.ui.util.APopUpViewModel
import com.example.popup.ui.util.UiEvent
import com.example.popup.ui.util.UiRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val apiService: IApiService
): APopUpViewModel<SignUpViewEvent>() {

    companion object {
        private val CREATE_ACCOUNT_ERROR: UiEvent.ShowError = UiEvent.ShowError(
            title = "Unable to Create Account",
            message = "We were unable to create your account, please try again later."
        )
    }

    // All the fun variables needed to make an account
    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var username by mutableStateOf("")
    var location by mutableStateOf(Location(0.0, 0.0))

    // Not a mutable state. Ideally, the "right" way to do this would to actually fetch the post
    // type preferences from the API, that way we could update preference types without having to
    // update the front-end. But this is a capstone so we will just be lazy and use the hardcoded
    // enum as the type list
    var preferences = mutableListOf<PostType>()
    var profilePicture: File? = null

    // Track which stage we are at in the process
    var stage: SignUpStage by mutableStateOf(SignUpStage.GET_STARTED)

    override fun onEvent(event: SignUpViewEvent) {
        when (event) {
            is SignUpViewEvent.OnReturnToLoginClicked -> {
                sendUiEventToChannel(event = UiEvent.Navigate(route = UiRoutes.LOGIN_SCREEN))
            }
            is SignUpViewEvent.OnCreateAccountClicked -> createUserAccount()
            is SignUpViewEvent.OnDefaultLocationChanged -> location = event.location
            is SignUpViewEvent.OnEmailChanged -> email = event.email
            is SignUpViewEvent.OnFirstnameChanged -> firstname = event.firstname
            is SignUpViewEvent.OnLastnameChanged -> lastname = event.lastname
            is SignUpViewEvent.OnPasswordChanged -> password = event.password
            is SignUpViewEvent.OnConfirmPasswordChanged -> confirmPassword = event.password
            is SignUpViewEvent.OnPreferencesChanged -> {
                handlePreferenceChange(event.preference, event.selected)
            }
            is SignUpViewEvent.OnUsernameChanged -> username = event.username
            is SignUpViewEvent.OnGetStartedClicked -> stage = SignUpStage.PREFERENCES
            is SignUpViewEvent.OnPreferenceNextClicked -> stage = SignUpStage.PERSONAL_INFORMATION
            is SignUpViewEvent.OnProfilePictureChanged -> profilePicture = event.picture
        }
    }

    /**
     * Handle creating the user account - this should ideally always work if we got past the first
     * part, but just in case, handle any errors here
     */
    private fun createUserAccount() {
        viewModelScope.launch {
            val request = CreateUserRequest(
                firstname = firstname,
                lastname = lastname,
                email = email,
                username = username,
                password = password,
                preferences = if (preferences.isEmpty()) null else preferences
            )

            val response = apiService.createUser(request, profilePicture)
            sendUiEventToChannel(
                event = when (response.wasSuccessful()) {
                    true -> UiEvent.Navigate(route = UiRoutes.MAIN_SCREEN)
                    false -> CREATE_ACCOUNT_ERROR
                }
            )
        }
    }

    /**
     * Handle a change in preferences. The value of the selected parameter determines if we are
     * adding or removing this from the list
     */
    private fun handlePreferenceChange(preference: PostType, selected: Boolean) {
        if (selected) {
            preferences.plus(preference)
        } else {
            val index = preferences.indexOf(element = preference)

            if (index != -1) {
                preferences.removeAt(index = index)
            }
        }
    }

}