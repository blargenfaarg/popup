package com.example.popup.ui.screens.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.networking.api.IApiService
import com.example.popup.di.NavigationHandler
import com.example.popup.ui.util.APopUpViewModel
import com.example.popup.ui.util.UiEvent
import com.example.popup.ui.util.UiRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * View model that is "shared" between the sign up views
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val apiService: IApiService,
    private val navigationHandler: NavigationHandler,
    private val signUpCache: SignUpCache
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

    // Post type
    private val _booleanStateMap = mutableStateMapOf<PostType, Boolean>().apply {
        PostType.entries.forEach { value ->
            this[value] = false
        }
    }
    val booleanStateMap: Map<PostType, Boolean> get() = _booleanStateMap
    private val preferences = mutableListOf<PostType>()

    // Profile picture
    private var profilePicture: File? = null

    // Loading dialog for create account progress
    var loading by mutableStateOf(false)

    override fun onEvent(event: SignUpViewEvent) {
        when (event) {
            is SignUpViewEvent.OnGoBackAttempted -> {

            }
            is SignUpViewEvent.OnReturnToLoginClicked -> {
                // We actually do not want the information to be saved here
                signUpCache.clearCache()
                navigationHandler.navigateBackToLoginScreen()
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
            is SignUpViewEvent.OnGetStartedClicked -> {
                signUpCache.cacheGettingStartedInfo(username, password, email)
                navigationHandler.navigateToRoute(UiRoutes.SIGN_UP_SCREEN_PREFERENCES)
            }
            is SignUpViewEvent.OnPreferenceNextClicked -> {
                signUpCache.cachePreferences(preferences)
                navigationHandler.navigateToRoute(UiRoutes.SIGN_UP_SCREEN_PERSONAL_INFO)
            }
            is SignUpViewEvent.OnProfilePictureChanged -> profilePicture = event.picture
        }
    }

    /**
     * Handle creating the user account - this should ideally always work if we got past the first
     * part, but just in case, handle any errors here
     */
    private fun createUserAccount() {
        loading = true

        viewModelScope.launch {
            val request = signUpCache.buildCreateUserRequest(firstname, lastname)
            val response = apiService.createUser(request, profilePicture)
            loading = false
            when (response.wasSuccessful()) {
                true -> navigationHandler.navigateBackToLoginScreen()
                false -> CREATE_ACCOUNT_ERROR
            }
        }
    }

    /**
     * Handle a change in preferences. The value of the selected parameter determines if we are
     * adding or removing this from the list
     */
    private fun handlePreferenceChange(preference: PostType, selected: Boolean) {
        _booleanStateMap[preference] = selected

        if (selected) {
            preferences.add(preference)
        } else {
            preferences.remove(preference)
        }
    }

}