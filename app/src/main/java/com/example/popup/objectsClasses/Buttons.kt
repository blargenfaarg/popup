package com.example.popup.objectsClasses

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.popup.R

@Composable
fun Buttons(){
    FilledTonalButton(
        modifier = Modifier
            .height(40.dp)
            .width(90.dp),
        onClick = { }) {//Used for pushed navigation button
        Text("Map")
    }

    OutlinedButton(
        modifier = Modifier
            .height(40.dp)
            .width(90.dp),
        onClick = { }) {//Used for non-pushed navigation button
        Text("List")
    }

    Box(
        modifier = Modifier
            .offset(x = 170.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.lil_triangle),//Insert proper images here
            contentDescription = "Map",
            modifier = Modifier
                .size(30.dp)
                .offset(x = -8.dp)
                .align(Alignment.CenterEnd)
        )
        OutlinedButton(
            modifier = Modifier
                .padding(8.dp),
            onClick = { /*TODO*/ }) {//Used for buttons with images
            Text("Filter")
        }
    }

    ExtendedFloatingActionButton(
        onClick = { },
        icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
        text = { Text(text = "Create Event") },
    )   //It has special shadow effect which is used for add new event
}