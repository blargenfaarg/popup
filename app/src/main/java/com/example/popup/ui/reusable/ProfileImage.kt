package com.example.popup.ui.reusable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import coil.compose.AsyncImage
import java.io.File

/**
 * Re-usable composable for showing a profile picture
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
@Composable
fun ProfileImage(
    allowPick: Boolean = true,
    onPick: (File) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            selectedImageUri = uri
            try {
                val file = uri.toFile()
                onPick(file)
            } catch (e: Exception) {
                println("Caught exception when converting file -> ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable {
                if (allowPick) {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        } else {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }
}