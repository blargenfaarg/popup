package com.example.popup.mainui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.reusable.PopUpTextField
import com.example.popup.ui.util.UiRoutes.CREATE_NAME_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_PLACE_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_REVIEW_SCREEN

@Composable
fun CreateMiscScreen(navController: NavController) {
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
                text = "Add photos and a description",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(32.dp))

            ExtendedFloatingActionButton(
                onClick = {  },
                icon = { Icon(Icons.Filled.Image, "Upload") },
                text = { Text(text = "Upload Photo(s)") },
            )

            Spacer(Modifier.height(32.dp))


            PopUpTextField(labelText = "Description", onValueChange = {selectedText = it}, text = selectedText)
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = {navController.navigate(CREATE_PLACE_SCREEN)}, text = "Back")
                PopUpPrimaryButton(onClick = {navController.navigate(CREATE_REVIEW_SCREEN)}, text = "Next")
            }
        }

        LoadNavBar(navController = navController)
    }
}

@Preview
@Composable
fun CreateMiscScreenPreview() {
    CreateMiscScreen(navController = rememberNavController())
}
