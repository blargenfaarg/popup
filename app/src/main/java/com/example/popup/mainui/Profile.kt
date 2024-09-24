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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    )
    {

        Card(modifier = Modifier.fillMaxWidth()
            .padding(12.dp)
            .height(160.dp))
        {
            Row(modifier=Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center)
            {
                Icon(Icons.Filled.AccountCircle,
                    contentDescription = "Icon",
                    modifier = Modifier.size(140.dp))
                Spacer(Modifier.padding(5.dp))
                Column(modifier=Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center)
                {
                    Text(text = "John Doe",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.SemiBold)
                    Button(onClick = {/*TODO: button functionality*/}) {
                        Text("Edit Profile")
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth().offset(y=180.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
            )
        {
            Text("Preference", fontSize = 24.sp)
            Spacer(Modifier.width(16.dp))
            ElevatedFilterChip(
                selected = false,
                onClick = { /*TODO*/ },
                label = { Text(text = "Select...") },
                trailingIcon = {Icon(Icons.Filled.ArrowDropDown, contentDescription = "Icon")}
            )
        }


        var state by remember {mutableStateOf(0)}
        val titles = listOf("Favorites", "History")

        Card (modifier = Modifier.fillMaxWidth()
            .height(525.dp)
            .offset(y = 230.dp)
            .padding(12.dp)
        ){
           TabRow(selectedTabIndex = state)
           {
               titles.forEachIndexed { index, title ->
                   Tab(
                       selected = state == index,
                       onClick = { state = index },
                       text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                   )
               }
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
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}