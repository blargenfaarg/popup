package com.example.popup.ui.reusable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.popup.ui.theme.GrayOutlinePrimary
import com.example.popup.ui.theme.TextPrimary
import com.example.popup.ui.util.UiConstants
import kotlin.coroutines.coroutineContext

/**
 * Custom buttons to use in the pop-up application. There are two buttons:
 * - PopUpPrimaryButton
 *      - Use this button for a primary action (e.g. Log in)
 * - PopUpSecondaryButton
 *      - Use this button for a secondary action (e.g. dismiss)
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
@Composable
fun PopUpPrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    buttonHorizontalPadding: Dp = UiConstants.BUTTON_HORIZONTAL_PADDING,
    textVerticalPadding: Dp = 7.dp,
    textHorizontalPadding: Dp = 15.dp,
    textFontSize: TextUnit = 20.sp
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(UiConstants.BUTTON_ROUNDED_CORNER_RADIUS),
        colors = popUpPrimaryButtonColors(),
        modifier = modifier
            .padding(horizontal = buttonHorizontalPadding)
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = TextPrimary
            )
        } else {
            Text(
                text = text,
                fontSize = textFontSize,
                modifier = Modifier
                    .padding(vertical = textVerticalPadding)
                    .padding(horizontal = textHorizontalPadding)
            )
        }
    }
}

@Composable
fun PopUpSecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    buttonHorizontalPadding: Dp = UiConstants.BUTTON_HORIZONTAL_PADDING,
    textVerticalPadding: Dp = 7.dp,
    textHorizontalPadding: Dp = 15.dp,
    textFontSize: TextUnit = 20.sp
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(1.dp, GrayOutlinePrimary),
        shape = RoundedCornerShape(UiConstants.BUTTON_ROUNDED_CORNER_RADIUS),
        colors = popUpSecondaryButtonColors(),
        modifier = modifier
            .padding(horizontal = buttonHorizontalPadding)
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = TextPrimary
            )
        } else {
            Text(
                text = text,
                fontSize = textFontSize,
                modifier = Modifier
                    .padding(vertical = textVerticalPadding)
                    .padding(horizontal = textHorizontalPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PopUpButtonPreviews() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PopUpPrimaryButton(
            modifier = Modifier
                .align(Alignment.Center),
            onClick = {
                Unit
            },
            text = "Log in"
        )
    }
}