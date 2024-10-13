package com.example.popup.mainui

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.reusable.PopUpTextField
import com.example.popup.ui.util.UiRoutes.CREATE_NAME_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_TIME_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_TYPE_SCREEN

@Composable
fun CreateTypeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "What type of event are you adding?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false)}
            var selectedText by remember { mutableStateOf("")}

            Box(){
                PopUpTextField(labelText = "Type of event...", text =selectedText, onValueChange = {selectedText = it})
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false},)
                {
                    DropdownMenuItem(text = {Text("Sale")}, onClick = {})
                    DropdownMenuItem(text = {Text("Sports")}, onClick = {})
                    DropdownMenuItem(text = {Text("Food")}, onClick = {})
                    DropdownMenuItem(text = {Text("Music")}, onClick = {})
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = {navController.navigate(CREATE_NAME_SCREEN)}, text = "Back")
                PopUpPrimaryButton(onClick = {navController.navigate(CREATE_TIME_SCREEN)}, text = "Next")
            }
        }

        LoadNavBar(navController = navController)
    }
}

@Preview
@Composable
fun CreateTypeScreenPreview() {
    CreateTypeScreen(navController = rememberNavController())
}
