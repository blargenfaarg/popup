package com.example.popup.mainui

import android.media.Image
import android.view.textclassifier.TextSelection
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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

@OptIn(ExperimentalMaterial3Api::class)
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
            Box (Modifier.offset(x = 180.dp)) {
                ElevatedFilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text(text = "Filter") },
                    trailingIcon = {Icon(Icons.Filled.ArrowDropDown, contentDescription = "Icon")}
                    )
            }
            Box  (Modifier.offset(x = 185.dp)){
                ElevatedFilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text(text = "Sort") },
                    trailingIcon = {Icon(Icons.Filled.ArrowDropDown, contentDescription = "Icon")})
            }
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
fun ListScreenPreview() {
    ListScreen(navController = rememberNavController())
}