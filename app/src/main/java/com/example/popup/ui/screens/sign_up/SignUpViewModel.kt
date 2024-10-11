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
import com.example.popup.model.request.OtpVerifyRequest
import com.example.popup.model.request.user.CreateUserRequest
import com.example.popup.model.request.user.CreateUserValidateRequest
import com.example.popup.ui.util.APopUpViewModel
import com.example.popup.ui.util.UiEvent
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
    private val navigationHandler: NavigationHandler
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
    var showBackWarning by mutableStateOf(false)

    // The stage for the view model
    var stage by mutableStateOf(SignUpStage.GET_STARTED)
        private set
    // To make life easier
    private var stageIndex: Int = 0
    private val stages = SignUpStage.entries.toTypedArray()

    // Control the next and previous arrows
    var showBackArrow by mutableStateOf(false)
    var showNextArrow by mutableStateOf(false)

    /**
     * Handle the events from the view
     */
    override fun onEvent(event: SignUpViewEvent) {
        when (event) {
            is SignUpViewEvent.OnGoBackAttempted -> {
                if (stage == SignUpStage.GET_STARTED) {
                    navigationHandler.navigateBackToLoginScreen()
                } else {
                    showBackWarning = true
                }
            }
            is SignUpViewEvent.OnReturnToLoginClicked -> {
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
            SignUpViewEvent.OnGetStartedClicked -> handleGetStarted()
            is SignUpViewEvent.OnProfilePictureChanged -> profilePicture = event.picture
            SignUpViewEvent.OnNextArrowClicked -> handleNextStageChange()
            SignUpViewEvent.OnPreviousArrowClicked -> handlePreviousStageChange()
            is SignUpViewEvent.OnOtpVerify -> handleOtpVerify(event.code)
        }
    }

    /**
     * Handle when the user submits the otp code
     */
    private fun handleOtpVerify(code: String) {
        viewModelScope.launch {
            val request = OtpVerifyRequest(
                email = email,
                otpCode = code
            )

            val response = apiService.verifyOtpCode(request)

            when (response.wasSuccessful()) {
                true -> handleNextStageChange()
                false -> {
                    if (response.error != null) {
                        sendUiEventToChannel(UiEvent.ShowError(
                            title = response.error.title,
                            message = response.error.message
                        ))
                    }
                }
            }
        }
    }

    /**
     * Method called when the user hits the get started button. will validate the creation params
     * and ensure no duplicate username or email, and then let them move on
     */
    private fun handleGetStarted() {
        loading = true

        viewModelScope.launch {
            val request = CreateUserValidateRequest(
                email = email,
                username = username
            )

            val response = apiService.validateUserCreationParams(request)
            loading = false

            when (response.wasSuccessful()) {
                true -> {
                    handleNextStageChange()
                    sendRequestForOtpCode()
                }
                false  -> {
                    if (response.error != null) {
                        sendUiEventToChannel(
                            UiEvent.ShowError(
                                title = response.error.title,
                                message = response.error.message
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Generate an otp code
     */
    private fun sendRequestForOtpCode() {
        viewModelScope.launch {
            val response = apiService.generateOtpCode(email = email)

            if (!response.wasSuccessful() && response.error != null) {
                sendUiEventToChannel(UiEvent.ShowError(
                    title = response.error.title,
                    message = response.error.message
                ))
            }
        }
    }

    /**
     * Handle creating the user account - this should ideally always work if we got past the first
     * part, but just in case, handle any errors here
     */
    private fun createUserAccount() {
        loading = true

        viewModelScope.launch {
            val request = CreateUserRequest(
                firstname = firstname,
                lastname = lastname,
                username = username,
                password = password,
                email = email,
                preferences = if (preferences.isEmpty()) null else preferences
            )

            val response = apiService.createUser(request, profilePicture)

            loading = false

            when (response.wasSuccessful()) {
                true -> navigationHandler.navigateBackToLoginScreen()
                false -> sendUiEventToChannel(CREATE_ACCOUNT_ERROR)
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

    /**
     * Handles changing to the next stage
     */
    private fun handleNextStageChange() {
        if (stageIndex < stages.size - 1) {
            stage = stages[++stageIndex]
            showNextArrow = SignUpStage.showNextArrow(stage)
            showBackArrow = SignUpStage.showBackArrow(stage)
        }
    }

    /**
     * Handles changing to the previous stage
     */
    private fun handlePreviousStageChange() {
        if (stageIndex > 0) {
            stage = stages[--stageIndex]
            showNextArrow = SignUpStage.showNextArrow(stage)
            showBackArrow = SignUpStage.showBackArrow(stage)
        }
    }

}