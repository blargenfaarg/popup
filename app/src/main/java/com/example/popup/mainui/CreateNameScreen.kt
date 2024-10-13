package com.example.popup.mainui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.reusable.PopUpTextField
import com.example.popup.ui.util.UiRoutes.CREATE_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_TYPE_SCREEN

@Composable
fun CreateNameScreen(navController: NavController) {
    
    var selectedText by remember { mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "What is your event?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            PopUpTextField(text = selectedText, onValueChange = {selectedText = it}, labelText = "Enter event name...")

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = {navController.navigate(CREATE_SCREEN)}, text = "Cancel")
                PopUpPrimaryButton(onClick = {
                    navController.navigate(CREATE_TYPE_SCREEN)
                                             }, text = "Next")
            }
        }

        LoadNavBar(navController = navController)
    }
}

@Preview
@Composable
fun CreateNameScreenPreview() {
    CreateNameScreen(navController = rememberNavController())
    }
