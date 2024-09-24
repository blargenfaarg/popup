package com.example.popup.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.popup.ui.theme.PopupTheme
import com.example.popup.ui.util.UiEvent

/**
 * The login screen view
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
@Composable
fun LoginView(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.ShowError -> {

                }
                else -> Unit
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Pop-Up",
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        LoginTextField(
            text = viewModel.username,
            labelText = "Username",
            onValueChange = {
                viewModel.onEvent(LoginViewEvent.OnUsernameChange(it))
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        LoginTextField(
            text = viewModel.password,
            labelText = "Password",
            onValueChange = {
                viewModel.onEvent(LoginViewEvent.OnPasswordChange(it))
            },
            passwordField = true
        )
        OutlinedButton(
            onClick = {
                viewModel.onEvent(LoginViewEvent.OnLoginClicked)
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Login")
        }
    }
}

@Composable
fun LoginTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    onValueChange: (String) -> Unit,
    passwordField: Boolean = false,
) {
    var showPassword by remember { mutableStateOf(false) }

    TextField(
        value = text,
        onValueChange = onValueChange,
        label = {
            Text(text = labelText)
        },
        visualTransformation = if (passwordField) {
            when (showPassword) {
                true -> VisualTransformation.None
                false -> PasswordVisualTransformation()
            }
        } else {
            VisualTransformation.None
        },
        modifier = modifier
            .padding(horizontal = 25.dp)
            .padding(16.dp),
        trailingIcon = {
            if (passwordField) {
                Icon(
                    imageVector = when (showPassword) {
                        true -> Icons.Filled.Visibility
                        false -> Icons.Filled.VisibilityOff
                    },
                    contentDescription = "Toggle password visibility",
                    modifier = Modifier
                        .clickable {
                            showPassword = !showPassword
                        }
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    PopupTheme {
        LoginView(
            onNavigate = {

            },
            viewModel = hiltViewModel()
        )
    }
}