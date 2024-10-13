package com.example.popup.mainui

import android.icu.util.Calendar
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.util.UiRoutes.CREATE_PLACE_SCREEN
import com.example.popup.ui.util.UiRoutes.CREATE_TYPE_SCREEN
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "When is your event?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))


            var showDatePicker by remember { mutableStateOf(false) }
            val datePickerState = rememberDatePickerState()
            val selectedDate = datePickerState.selectedDateMillis?.let {
                convertToDate(it)
            } ?: ""

            Box(Modifier.fillMaxWidth())
            {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    label = { Text(text = "Date", Modifier.fillMaxSize()) },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = !showDatePicker })
                        {
                           Icon(imageVector = Icons.Default.DateRange,
                               contentDescription = "A calendar icon")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding (4.dp)
                )
                if (showDatePicker)
                {
                    Dialog(onDismissRequest = {showDatePicker = false})
                    {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp))
                        {
                            DatePicker(state = datePickerState,
                                showModeToggle = false)
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = {navController.navigate(CREATE_TYPE_SCREEN)}, text = "Back")
                PopUpPrimaryButton(
                    onClick = { navController.navigate(CREATE_PLACE_SCREEN) },
                    text = "Next"
                )
            }
        }

        LoadNavBar(navController = navController)
    }
}

fun convertToDate(millis: Long): String // outputs 1 day before in TextField, don't know why
{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    return formatter.format(calendar.time)
}


@Preview
@Composable
fun CreateTimeScreenPreview() {
    CreateTimeScreen(navController = rememberNavController())
}
