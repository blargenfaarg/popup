package com.example.popup.mainui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.Screen

@Composable
fun ProfileScreen(
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
        ) {
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.mapScreen.route) }) {//Nav to other screens
                Text("Map")
            }
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.listScreen.route) }) {//Nav to other screens
                Text("List")
            }
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.createScreen.route) }) {//Nav to other screens
                Text("Create")
            }
            FilledTonalButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.profileScreen.route) }) {//Nav to other screens
                Text("Profile")
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}