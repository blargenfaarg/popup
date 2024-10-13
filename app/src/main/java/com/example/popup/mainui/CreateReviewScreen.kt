package com.example.popup.mainui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.util.UiRoutes.CREATE_MISC_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_TIME_SCREEN
import com.example.popup.ui.util.UiRoutes.LIST_SCREEN

@Composable
fun CreateReviewScreen(navController: NavController) {
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
                text = "Review your submission",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            OutlinedCard(Modifier.fillMaxWidth().height(512.dp).padding(16.dp))
            {
                Row(Modifier.fillMaxWidth().padding(16.dp))
                {
                    Icon(imageVector = Icons.Filled.PersonPin,
                        contentDescription = "A calendar icon",
                        modifier = Modifier.size(64.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(verticalArrangement = Arrangement.Center)
                    {
                        Text("Event Name", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
                        Spacer(Modifier.height(12.dp))
                        Text("Event Type")
                    }

                }
                Column(Modifier.fillMaxWidth()
                    .height(256.dp)
                    .background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                {
                    Image(imageVector = Icons.Default.Image, contentDescription = "Test",
                        Modifier.size(128.dp), alpha = 0.5f)
                }
                Column(Modifier.fillMaxWidth().padding(16.dp))
                {
                    Text("Address", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("Date | Time")
                    Spacer(Modifier.height(16.dp))
                    Text("Description")
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = {navController.navigate(CREATE_MISC_SCREEN)}, text = "Back")
                PopUpPrimaryButton(onClick = {navController.navigate(LIST_SCREEN)}, text = "Submit")
            }
        }

        LoadNavBar(navController = navController)
    }
}

@Preview
@Composable
fun CreateReviewScreenPreview() {
    CreateReviewScreen(navController = rememberNavController())
}
