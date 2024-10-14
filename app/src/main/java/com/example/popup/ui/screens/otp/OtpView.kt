package com.example.popup.ui.screens.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.screens.sign_up.SignUpViewPageHeading
import com.example.popup.ui.theme.GrayOutlinePrimary

@Composable
fun OtpView(
    email: String,
    onCodeSubmit: (String) -> Unit
) {
    var otpText by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignUpViewPageHeading(
            title = "Verify Email",
            description = "We sent a code to $email, it should arrive within a few minutes. Enter " +
                    "the code here to verify your email address."
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        BasicTextField(
            value = otpText,
            onValueChange = {
                if (it.length <= 6) {
                    otpText = it
                }
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(6) { index ->
                    val number = when {
                        index >= otpText.length -> ""
                        else -> otpText[index]
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(1.5.dp)
                                .background(GrayOutlinePrimary)
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(100.dp)
        )

        PopUpPrimaryButton(
            buttonHorizontalPadding = 0.dp,
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                onCodeSubmit(otpText)
            },
            text = "Verify"
        )
    }

}

@Composable
@Preview(showBackground = true)
fun OptViewPreview() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        OtpView(
            email = "spiderman@ny.com",
            onCodeSubmit = {

            }
        )
    }
}