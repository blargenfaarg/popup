package com.example.popup.ui.screens.sign_up

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import coil.compose.AsyncImage
import com.example.popup.mock.MockApiService
import com.example.popup.model.domain.common.PostType
import com.example.popup.ui.reusable.PopUpErrorHandler
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpProtectedTextField
import com.example.popup.ui.reusable.PopUpTextField
import com.example.popup.di.NavigationHandler
import com.example.popup.ui.reusable.PopUpErrorDialog
import com.example.popup.ui.theme.BluePrimary
import com.example.popup.ui.theme.GrayOutlineSecondary
import com.example.popup.ui.util.UiEvent
import com.example.popup.ui.util.clearFocusOnTap
import com.google.maps.android.compose.GoogleMap

/**
 * Composable to show the getting started screen
 */
@Composable
fun GetStartedSignUpView(
    viewModel: SignUpViewModel
) {
    var errorEvent: UiEvent.ShowError? by remember { mutableStateOf(null) }
    var showGoBackWarning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowError -> errorEvent = event
                else -> Unit
            }
        }
    }

    BackHandler {
        showGoBackWarning = true
    }

    // Shows an errors that happened
    PopUpErrorHandler(
        event = errorEvent,
        negativeText = "Okay",
        onDismiss = {
            errorEvent = null
        }
    )

    // Show the back button warning - let the user know they will lose their data if they leave
    if (showGoBackWarning) {
        PopUpErrorDialog(
            title = "Data Loss Warning",
            body = "If you leave, you will lose any entered data. To navigate during account creation, use the buttons on the bottom half of the screen.",
            negativeText = "Leave",
            positiveText = "Stay",
            onDismiss = {},
            onConfirm = {
                viewModel.onEvent(event = SignUpViewEvent.OnReturnToLoginClicked)
            }
        )
    }

    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var mismatchingPasswords by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .clearFocusOnTap(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SignUpViewPageHeading(
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth(),
            title = "Sign Up",
            description = "Get started by making a new account"
        )
        PopUpTextField(
            text = viewModel.username,
            isError = usernameError,
            labelText = "Username",
            horizontalPadding = 0.dp,
            onValueChange = {
                usernameError = false
                viewModel.onEvent(SignUpViewEvent.OnUsernameChanged(it))
            }
        )
        PopUpTextField(
            text = viewModel.email,
            isError = emailError,
            labelText = "Email",
            horizontalPadding = 0.dp,
            onValueChange = {
                emailError = false
                viewModel.onEvent(SignUpViewEvent.OnEmailChanged(it))
            }
        )
        PopUpProtectedTextField(
            text = viewModel.password,
            labelText = "Password",
            isError = passwordError || mismatchingPasswords,
            horizontalPadding = 0.dp,
            onValueChange = {
                mismatchingPasswords = false
                passwordError = false
                viewModel.onEvent(SignUpViewEvent.OnPasswordChanged(it))
            }
        )
        PopUpProtectedTextField(
            text = viewModel.confirmPassword,
            isError = confirmPasswordError || mismatchingPasswords,
            labelText = "Confirm Password",
            horizontalPadding = 0.dp,
            onValueChange = {
                mismatchingPasswords = false
                confirmPasswordError = false
                viewModel.onEvent(SignUpViewEvent.OnConfirmPasswordChanged(it))
            }
        )

        Spacer(
            modifier = Modifier.size(35.dp)
        )

        PopUpPrimaryButton(
            onClick = {
                usernameError = viewModel.username.isBlank()
                emailError = viewModel.email.isBlank()
                passwordError = viewModel.password.isBlank()
                confirmPasswordError = viewModel.confirmPassword.isBlank()

                if (!usernameError && !emailError && !passwordError && !confirmPasswordError) {
                    if (viewModel.password != viewModel.confirmPassword) {
                        mismatchingPasswords = true
                    } else {
                        viewModel.onEvent(event = SignUpViewEvent.OnGetStartedClicked)
                    }
                }
            },
            text = "Get Started",
            buttonHorizontalPadding = 0.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Login",
            color = BluePrimary,
            fontSize = 18.sp,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .clickable {
                    viewModel.onEvent(event = SignUpViewEvent.OnReturnToLoginClicked)
                }
        )
    }
}

/**
 * Composable to show the preference selection screen
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesSelectionSignUpView(
    viewModel: SignUpViewModel
) {
    val preferences = viewModel.booleanStateMap

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SignUpViewPageHeading(
            title = "Select Preferences",
            description = "Choose which types of posts you are most interested in, allowing us to deliver" +
                    " you the best results",
            titleModifier = Modifier.padding(horizontal = 20.dp),
            descriptionModifier = Modifier.padding(horizontal = 20.dp)
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            for (preference in preferences) {
                PreferenceSelection(
                    viewModel = viewModel,
                    preference = preference.key,
                    selected = preference.value
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = {
                viewModel.onEvent(event = SignUpViewEvent.OnPreferenceNextClicked)
            },
            containerColor = BluePrimary,
            contentColor = GrayOutlineSecondary,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .width(85.dp)
                .height(40.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                contentDescription = "Go to next screen",
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }
}

/**
 * Composable to show a single preference selection
 */
@Composable
fun PreferenceSelection(
    viewModel: SignUpViewModel,
    preference: PostType,
    selected: Boolean
) {
    Surface(
        modifier = Modifier
            .clickable {
                viewModel.onEvent(
                    event = SignUpViewEvent.OnPreferencesChanged(
                        preference = preference, selected = !selected
                    )
                )
            }
            .padding(8.dp),
        color = if (selected) BluePrimary else GrayOutlineSecondary,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, color = GrayOutlineSecondary)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Text(
                text = preference.prettyName,
                color = if (selected) Color.White else Color.Black,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun PersonalInformationSignUpView(
    viewModel: SignUpViewModel
) {
    var firstnameError by remember { mutableStateOf(false) }
    var lastnameError by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            viewModel.onEvent(event = SignUpViewEvent.OnProfilePictureChanged(
                picture = uri.toFile()
            ))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .clearFocusOnTap()
            .blur(if (viewModel.loading) 3.dp else 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            SignUpViewPageHeading(
                title = "Personal Information",
                description = "Information about you that others will get to see",
                titleModifier = Modifier.align(alignment = Alignment.Start)
            )
        }
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .size(20.dp)
        )
        PopUpTextField(
            text = viewModel.firstname,
            isError = firstnameError,
            labelText = "Firstname",
            horizontalPadding = 0.dp,
            onValueChange = {
                firstnameError = false
                viewModel.onEvent(SignUpViewEvent.OnFirstnameChanged(it))
            }
        )
        PopUpTextField(
            text = viewModel.lastname,
            isError = lastnameError,
            labelText = "Lastname",
            horizontalPadding = 0.dp,
            onValueChange = {
                lastnameError = false
                viewModel.onEvent(SignUpViewEvent.OnLastnameChanged(it))
            }
        )
        Spacer(
            modifier = Modifier
                .size(35.dp)
        )
        PopUpPrimaryButton(
            modifier = Modifier
                .fillMaxWidth(),
            buttonHorizontalPadding = 0.dp,
            onClick = {
                firstnameError = viewModel.firstname.isBlank()
                lastnameError = viewModel.lastname.isBlank()

                if (!firstnameError && !lastnameError) {
                    viewModel.onEvent(event = SignUpViewEvent.OnCreateAccountClicked)
                }
            },
            text = "Create Account"
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.loading) {
            CircularProgressIndicator(
                color = BluePrimary
            )
        }
    }
}

/**
 * Composable that creates a heading for the page
 */
@Composable
fun SignUpViewPageHeading(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    titleModifier: Modifier = Modifier,
    descriptionModifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = titleModifier
        )
        Text(
            text = description,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = descriptionModifier
        )

        Spacer(
            modifier = Modifier.size(35.dp)
        )
    }
}

/**
 * Composable for the location selection
 */
@Composable
fun LocationSelectionSignUpView(
    viewModel: SignUpViewModel
) {
    var isMapLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapLoaded = {
                isMapLoaded = true
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSelectionSignUpViewPreview() {
    LocationSelectionSignUpView(
        viewModel = SignUpViewModel(
            apiService = MockApiService(),
            navigationHandler = NavigationHandler(),
            signUpCache = SignUpCache()
        )
    )
}


//@Preview(showBackground = true)
@Composable
fun PersonalInformationSignUpViewPreview() {
    PersonalInformationSignUpView(
        viewModel = SignUpViewModel(
            apiService = MockApiService(),
            navigationHandler = NavigationHandler(),
            signUpCache = SignUpCache()
        )
    )
}

//@Preview(showBackground = true)
@Composable
fun PreferenceSelectionPreview() {
    PreferenceSelection(
        viewModel = SignUpViewModel(
            apiService = MockApiService(),
            navigationHandler = NavigationHandler(),
            signUpCache = SignUpCache()
        ),
        preference = PostType.YARD_SALE,
        selected = false
    )
}

//@Preview(showBackground = true)
@Composable
fun PreferencesSelectionSignUpViewPreview() {
    PreferencesSelectionSignUpView(
        viewModel = SignUpViewModel(
            apiService = MockApiService(),
            navigationHandler = NavigationHandler(),
            signUpCache = SignUpCache()
        )
    )
}

//@Preview(showBackground = true)
@Composable
fun GetStartedSignUpViewPreview() {
    GetStartedSignUpView(
        viewModel = SignUpViewModel(
            apiService = MockApiService(),
            navigationHandler = NavigationHandler(),
            signUpCache = SignUpCache()
        )
    )
}