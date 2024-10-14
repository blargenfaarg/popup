package com.example.popup.mainui

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.popup.ui.reusable.PopUpPrimaryButton
import com.example.popup.ui.reusable.PopUpSecondaryButton
import com.example.popup.ui.reusable.PopUpTextField
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateScreen(navController: NavController) {

    val createNavController = rememberNavController()
    NavHost(navController = createNavController, startDestination = "step0"){
        composable("step0") {Step0Screen(createNavController)}
        composable("step1") {NameScreen(createNavController)}
        composable("step2") {TypeScreen(createNavController)}
        composable("step3") {DateScreen(createNavController)}
        composable("step4") {PlaceScreen(createNavController)}
        composable("step5") {MiscScreen(createNavController)}
        composable("step6") {ConfirmScreen(createNavController)}
    }


}

@Composable
fun Step0Screen(navController: NavController)
{
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White))
    {
        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(15.dp)
            .offset(y = -100.dp)
        ) {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("step1") },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Create Event") },
            )
        }
    }
}


@Composable
fun NameScreen(navController: NavController)
{
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
                text = "What is your event?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            PopUpTextField(
                text = selectedText,
                onValueChange = { selectedText = it },
                labelText = "Enter event name..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = {navController.navigate("step0") }, text = "Cancel")
                PopUpPrimaryButton(onClick = {navController.navigate("step2")}, text = "Next")
            }
        }
    }
}

@Composable
fun TypeScreen(navController : NavController)
{
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
                text = "What type of event are you adding?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }
            var selectedText by remember { mutableStateOf("") }

            Box() {
                PopUpTextField(
                    labelText = "Type of event...",
                    text = selectedText,
                    onValueChange = { selectedText = it })
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },)
                {
                    DropdownMenuItem(text = { Text("Sale") }, onClick = {})
                    DropdownMenuItem(text = { Text("Sports") }, onClick = {})
                    DropdownMenuItem(text = { Text("Food") }, onClick = {})
                    DropdownMenuItem(text = { Text("Music") }, onClick = {})
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(
                    onClick = { navController.navigate("step1") },
                    text = "Back"
                )
                PopUpPrimaryButton(
                    onClick = { navController.navigate("step3") },
                    text = "Next"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateScreen(navController : NavController) {
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
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "A calendar icon"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(4.dp)
                )
                if (showDatePicker) {
                    Dialog(onDismissRequest = { showDatePicker = false })
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(elevation = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp)
                        )
                        {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false
                            )
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = { navController.navigate("step2") }, text = "Back")
                PopUpPrimaryButton(
                    onClick = { navController.navigate("step4") },
                    text = "Next"
                )
            }
        }
    }
}

fun convertToDate(millis: Long): String // outputs 1 day before in TextField, don't know why
{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis

    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    return formatter.format(calendar.time)
}

@Composable
fun PlaceScreen(navController : NavController)
{
    var selectedPlace by remember { mutableStateOf("")}
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
                text = "Where is your event?",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))

            PopUpTextField(
                labelText = "Enter a location",
                onValueChange = { selectedPlace = it },
                text = selectedPlace,
                icon = Icons.Default.Map
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(onClick = { navController.navigate("step3") }, text = "Back")
                PopUpPrimaryButton(onClick = { navController.navigate("step5") }, text = "Next")
            }
        }
    }
}

@Composable
fun MiscScreen(navController : NavController)
{
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
                onClick = { },
                icon = { Icon(Icons.Filled.Image, "Upload") },
                text = { Text(text = "Upload Photo(s)") },
            )

            Spacer(Modifier.height(32.dp))


            PopUpTextField(
                labelText = "Description",
                onValueChange = { selectedText = it },
                text = selectedText
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                PopUpSecondaryButton(
                    onClick = { navController.navigate("step4") },
                    text = "Back"
                )
                PopUpPrimaryButton(
                    onClick = { navController.navigate("step6") },
                    text = "Next"
                )
            }
        }
    }
}

@Composable
fun ConfirmScreen(navController : NavController)
{
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
                    Icon(imageVector = Icons.Filled.AccountCircle,
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
                PopUpSecondaryButton(onClick = {navController.navigate("step5")}, text = "Back")
                PopUpPrimaryButton(onClick = {navController.navigate("step0")}, text = "Submit")
            }
        }
    }
}


@Preview
@Composable
fun Preview() {
    ConfirmScreen(navController = rememberNavController())
}

