package com.example.popup.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.popup.R
import com.example.popup.di.NavigationHandler
import com.example.popup.mock.MockApiService
import com.example.popup.ui.reusable.PopUpErrorHandler
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpProtectedTextField
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.reusable.PopUpTextField
import com.example.popup.ui.theme.GrayOutlinePrimary
import com.example.popup.ui.theme.PopUpLightBlue
import com.example.popup.ui.theme.PopupTheme
import com.example.popup.ui.util.UiConstants
import com.example.popup.ui.util.UiEvent
import com.example.popup.ui.util.clearFocusOnTap

/**
 * The login screen view
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
@Composable
fun LoginView(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loading by viewModel.loading.collectAsState()
    var errorEvent: UiEvent.ShowError? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowError -> errorEvent = event
                else -> Unit
            }
        }
    }

    PopUpErrorHandler(
        event = errorEvent,
        negativeText = "Okay",
        onDismiss = {
            errorEvent = null
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap()
            .padding(15.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.world_with_pin),
            contentDescription = "PopUp Icon"
        )
        Spacer(
            modifier = Modifier
                .height(35.dp)
        )
        Text(
            text = UiConstants.PROJECT_NAME,
            fontSize = 34.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 20.dp)
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        PopUpTextField(
            text = viewModel.username,
            labelText = "Username",
            onValueChange = {
                viewModel.onEvent(LoginViewEvent.OnUsernameChange(it))
            }
        )
        PopUpProtectedTextField(
            text = viewModel.password,
            labelText = "Password",
            onValueChange = {
                viewModel.onEvent(LoginViewEvent.OnPasswordChange(it))
            }
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Text(
            text = "Forgot password?",
            fontSize = 14.sp,
            color = PopUpLightBlue,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .align(alignment = Alignment.End)
        )
        Spacer(
            modifier = Modifier
                .height(25.dp)
        )
        PopUpPrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.onEvent(LoginViewEvent.OnLoginClicked)
            },
            text = "Login",
            loading = loading
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .padding(start = 20.dp),
                color = GrayOutlinePrimary
            )
            Text(
                text = "or",
                color = GrayOutlinePrimary,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .padding(end = 20.dp),
                color = GrayOutlinePrimary
            )
        }
        PopUpSecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.onEvent(event = LoginViewEvent.OnSignUpClicked)
            },
            text = "Sign up"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    PopupTheme {
        LoginView(
            //onNavigate = { },
            viewModel = LoginViewModel(
                apiService = MockApiService(),
                navigationHandler = NavigationHandler()
            )
        )
    }
}