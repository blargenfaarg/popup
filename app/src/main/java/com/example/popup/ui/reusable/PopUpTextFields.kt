package com.example.popup.ui.reusable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.popup.ui.util.UiConstants

/**
 * Custom Pop-Up text fields
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
@Composable
fun PopUpTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    iconAction: (() -> Unit)? = null,
    isError: Boolean = false,
    horizontalPadding: Dp = UiConstants.TEXT_FIELD_HORIZONTAL_PADDING,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        colors = popUpOutlinedTextFieldColors(),
        label = {
            Text(text = labelText)
        },
        visualTransformation = visualTransformation,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        trailingIcon = {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            if (iconAction != null) {
                                iconAction()
                            }
                        }
                )
            }
        },
        shape = RoundedCornerShape(UiConstants.TEXT_FIELD_CORNER_RADIUS),
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun PopUpProtectedTextField(
    modifier: Modifier = Modifier,
    text: String,
    labelText: String,
    onValueChange: (String) -> Unit,
    showingIcon: ImageVector = Icons.Filled.Visibility,
    hidingIcon: ImageVector = Icons.Filled.VisibilityOff,
    horizontalPadding: Dp = UiConstants.TEXT_FIELD_HORIZONTAL_PADDING,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var showText by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        colors = popUpOutlinedTextFieldColors(),
        label = {
            Text(text = labelText)
        },
        visualTransformation = if (showText) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        trailingIcon = {
            Icon(
                imageVector = when (showText) {
                    true -> showingIcon
                    false -> hidingIcon
                },
                contentDescription = "Toggle text visibility",
                modifier = Modifier
                    .clickable {
                        showText = !showText
                    }
            )
        },
        shape = RoundedCornerShape(UiConstants.TEXT_FIELD_CORNER_RADIUS),
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}