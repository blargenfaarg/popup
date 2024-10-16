package com.example.popup.ui.reusable

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.popup.ui.theme.BluePrimary
import com.example.popup.ui.theme.GrayOutlinePrimary
import com.example.popup.ui.theme.GrayOutlineSecondary
import com.example.popup.ui.theme.Poiple
import com.example.popup.ui.theme.PopUpLightBlue
import com.example.popup.ui.theme.PopUpLightGray
import com.example.popup.ui.theme.TextPrimary

/**
 * Button colors for the pop-up buttons
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
@Composable
fun popUpPrimaryButtonColors(
    backgroundColor: Color = PopUpLightBlue,
    contentColor: Color = PopUpLightGray,
    disabledBackgroundColor: Color = GrayOutlinePrimary,
    disabledContentColor: Color = GrayOutlineSecondary
) = ButtonDefaults.buttonColors(
    containerColor = backgroundColor,
    contentColor = contentColor,
    disabledContainerColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor
)

@Composable
fun popUpSecondaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = Color.White,
    contentColor = GrayOutlinePrimary
)

@Composable
fun popUpOutlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GrayOutlinePrimary,
    unfocusedBorderColor = GrayOutlinePrimary
)