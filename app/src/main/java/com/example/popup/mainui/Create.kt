package com.example.popup.mainui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.util.UiRoutes.CREATE_NAME_SCREEN

@Composable
fun CreateScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    )
    {
        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(15.dp)
            .offset(y = -100.dp)
        ) {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(CREATE_NAME_SCREEN) },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Create Event") },
            )
        }

        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
        ) {
            LoadNavBar(navController = navController)
        }

    }
}

@Preview
@Composable
fun CreateScreenPreview() {
    CreateScreen(navController = rememberNavController())
}