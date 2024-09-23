package com.example.popup.mainui

import android.media.Image
import android.view.textclassifier.TextSelection
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.R
import com.example.popup.Screen

@Composable
fun ListScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    )
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .padding(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = 170.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.lil_triangle),//TODO: Find proper image from R
                    contentDescription = "Map",
                    modifier = Modifier
                        .size(30.dp)
                        .offset(x = -8.dp)
                        .align(Alignment.CenterEnd)
                )
                OutlinedButton(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = { /*TODO*/ }) {//TODO:Pop-up list if selections for filter
                    Text("Filter")
                }
            }
            Box(
                modifier = Modifier
                    .offset(x = 170.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.lil_triangle),//TODO: Find proper image from R
                    contentDescription = "Map",
                    modifier = Modifier
                        .size(30.dp)
                        .offset(x = -8.dp)
                        .align(Alignment.CenterEnd)
                )
                OutlinedButton(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = { /*TODO*/ }) {//TODO:Pop-up list if selections for filter
                    Text(" Sort ")
                }
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(15.dp)
        ) {
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.mapScreen.route) }) {//Nav to other screens
                Text("Map")
            }
            FilledTonalButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.listScreen.route) }) {//Nav to other screens
                Text("List")
            }
            OutlinedButton(
                modifier = Modifier
                    .width(90.dp),
                onClick = {navController.navigate(route = Screen.createScreen.route) }) {// Nav to other screens
                Text("Create")
            }
            OutlinedButton(
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
fun ListScreenPreview() {
    ListScreen(navController = rememberNavController())
}