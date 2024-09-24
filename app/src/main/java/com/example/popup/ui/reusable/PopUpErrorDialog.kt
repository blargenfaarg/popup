package com.example.popup.ui.reusable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.popup.ui.theme.GrayOutlinePrimary

/**
 * A Pop-Up error dialog
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
@Composable
fun PopUpErrorDialog(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    onDismiss: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    negativeText: String,
    positiveText: String? = null
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismiss?.let { it() }
        },
        containerColor = Color.White,
        titleContentColor = Color.Black,
        textContentColor = GrayOutlinePrimary,
        confirmButton = {
            if (positiveText != null) {
                PopUpPrimaryButton(
                    onClick = {
                        onConfirm?.let { it() }
                    },
                    text = positiveText,
                    buttonHorizontalPadding = 2.dp,
                    textFontSize = 14.sp,
                    textHorizontalPadding = 2.dp,
                    textVerticalPadding = 2.dp
                )
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = body)
        },
        dismissButton = {
            PopUpSecondaryButton(
                onClick = {
                    onDismiss?.let { it() }
                },
                text = negativeText,
                buttonHorizontalPadding = 2.dp,
                textFontSize = 14.sp,
                textHorizontalPadding = 2.dp,
                textVerticalPadding = 2.dp
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PopUpErrorDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PopUpErrorDialog(
            title = "Error",
            body = "Unable to complete sign in",
            positiveText = "Okay",
            negativeText = "Cancel"
        )
    }
}