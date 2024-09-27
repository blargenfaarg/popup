package com.example.popup.ui.screens.sign_up

import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.ui.util.ViewModelEvent
import java.io.File

/**
 * Events that can occur in the sign up screen
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
sealed class SignUpViewEvent: ViewModelEvent {
    data object OnGoBackAttempted: SignUpViewEvent()
    data object OnGetStartedClicked: SignUpViewEvent()
    data object OnPreferenceNextClicked: SignUpViewEvent()
    data object OnCreateAccountClicked: SignUpViewEvent()
    data object OnReturnToLoginClicked: SignUpViewEvent()
    data class OnFirstnameChanged(val firstname: String): SignUpViewEvent()
    data class OnLastnameChanged(val lastname: String): SignUpViewEvent()
    data class OnEmailChanged(val email: String): SignUpViewEvent()
    data class OnUsernameChanged(val username: String): SignUpViewEvent()
    data class OnPasswordChanged(val password: String): SignUpViewEvent()
    data class OnConfirmPasswordChanged(val password: String): SignUpViewEvent()
    data class OnProfilePictureChanged(val picture: File): SignUpViewEvent()
    data class OnDefaultLocationChanged(val location: Location): SignUpViewEvent()
    data class OnPreferencesChanged(
        val preference: PostType,
        val selected: Boolean
    ): SignUpViewEvent()
}